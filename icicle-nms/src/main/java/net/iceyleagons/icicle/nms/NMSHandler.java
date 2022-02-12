/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.nms;

import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.nms.annotations.*;
import net.iceyleagons.icicle.nms.annotations.constructor.Constructor;
import net.iceyleagons.icicle.nms.annotations.version.Version;
import net.iceyleagons.icicle.nms.annotations.version.alt.Alternative;
import net.iceyleagons.icicle.nms.utils.ClassHelper;
import net.iceyleagons.icicle.nms.utils.MethodDelegator;
import net.iceyleagons.icicle.utilities.AdvancedClass;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static net.iceyleagons.icicle.nms.NMSHelper.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 27, 2021
 */
public class NMSHandler {

    private final ByteBuddy byteBuddy;

    public NMSHandler(Application application) {
        this.byteBuddy = application.getBeanManager().getProxyHandler().getProxy();
    }

    @SneakyThrows
    public <T> T wrap(Class<T> toWrap, int constructorId, Object... params) {
        Constructor[] constructors = toWrap.getAnnotationsByType(Constructor.class);
        Constructor constructorToUse = null;

        for (Constructor constructor : constructors) {
            if (constructor.id() == constructorId) {
                constructorToUse = constructor;
                break;
            }
        }

        if (constructorToUse == null) {
            throw new IllegalStateException("Constructor with id " + constructorId + " not found!");
        }

        final AdvancedClass<?> clazz = getWrapClass(toWrap);
        Object origin = clazz.getClazz().getDeclaredConstructor(getParameterClasses(constructorToUse)).newInstance(params);

        return wrapFromOrigin(origin, toWrap);
    }

    @SneakyThrows
    public <T> T wrapFromOrigin(Object origin, Class<T> toWrap) {
        if (toWrap == null || !toWrap.isInterface()) {
            throw new IllegalArgumentException("toWrap must be an interface!");
        }

        final AdvancedClass<?> clazz = getWrapClass(toWrap);
        if (clazz == null) {
            throw new IllegalStateException("Could not create wrap for: " + toWrap.getName() + " , because the to-be-wrapped class is not found!");
        }

        final Map<Method, WrapSupplier<Object>> suppliers = new HashMap<>();
        final Method[] methods = getMethodsForCorrectVersion(toWrap);

        for (Method declaredMethod : methods) {
            if (declaredMethod.isAnnotationPresent(Wrapping.class)) {
                Wrapping wrapping = declaredMethod.getAnnotation(Wrapping.class);
                suppliers.put(declaredMethod, handleMethodWrapping(origin, wrapping, clazz, declaredMethod));
            } else if (declaredMethod.isAnnotationPresent(FieldWrapping.class)) {
                FieldWrapping wrapping = declaredMethod.getAnnotation(FieldWrapping.class);
                suppliers.put(declaredMethod, handleFieldWrapping(origin, wrapping, clazz, declaredMethod));
            } else if (declaredMethod.isAnnotationPresent(OriginGetter.class)) {
                suppliers.put(declaredMethod, WrapSupplier.createOriginSupplier(origin));
            }
        }

        return createObject(toWrap, suppliers);
    }

    private Method[] getMethodsForCorrectVersion(Class<?> root) {
        if (!root.isAnnotationPresent(Version.class)) {
            return root.getDeclaredMethods();
        }

        Class<?> wrapper = null;
        final String version = ClassHelper.VERSION;

        for (Version v : root.getAnnotationsByType(Version.class)) {
            for (String s : v.versions()) {
                if (s.equals(version)) {
                    wrapper = v.wrapper();
                    break;
                }
            }
        }

        if (wrapper == null) {
            throw new IllegalStateException("Could not find matching wrapper of " + root.getName() + " for version: " + version);
        }

        if (!wrapper.isInterface()) {
            throw new IllegalStateException("Found wrapper of " + root.getName() + " for version " + version + " is not an interface!");
        }

        return wrapper.getDeclaredMethods();
    }

    private <T> T createObject(Class<T> clazz, Map<Method, WrapSupplier<Object>> suppliers) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DynamicType.Builder<T> builder = byteBuddy.subclass(clazz);
        for (Map.Entry<Method, WrapSupplier<Object>> methodWrapSupplierEntry : suppliers.entrySet()) {
            Method method = methodWrapSupplierEntry.getKey();
            WrapSupplier<Object> supplier = methodWrapSupplierEntry.getValue();

            if (method.isAnnotationPresent(FieldWrapping.class)) {
                builder = builder.define(method).intercept(FixedValue.value(supplier.supply(null)));
                continue;
            }

            builder = builder.define(method).intercept(MethodDelegation.to(new MethodDelegator(supplier)));
        }

        return builder.make()
                .load(clazz.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded().getDeclaredConstructor().newInstance();
    }

    private String getProperNameForVersion(Method method, String current) {
        if (!method.isAnnotationPresent(Alternative.class)) {
            return current;
        }

        final String version = ClassHelper.VERSION;
        String alt = null;

        for (Alternative alternative : method.getAnnotationsByType(Alternative.class)) {
            if (alternative.version().equals(version)) {
                alt = alternative.value();
                break;
            }
        }

        if (alt == null || alt.isEmpty()) {
            throw new IllegalStateException("Invalid alternative mapping for method: " + method.getName() + " for version: " + version + " (null or empty)");
        }

        return alt;
    }

    private WrapSupplier<Object> handleFieldWrapping(Object origin, FieldWrapping wrapping, AdvancedClass<?> clazz, Method declaredMethod) {
        final String name = getProperNameForVersion(declaredMethod, wrapping.value());
        if (wrapping.isSetter()) {
            if (declaredMethod.getParameterTypes().length != 1) {
                throw new IllegalStateException("Setter @FieldWrapping must have exactly one parameter!");
            }

            return new WrapSupplier<>(origin) {
                @Override
                public Object supply(Object[] params) {
                    clazz.setFieldValue(name, this.getOrigin(), params[0]);
                    return null;
                }
            };
        }

        return new WrapSupplier<>(origin) {
            @Override
            public Object supply(Object[] params) {
                Object obj = clazz.getFieldValue(name, this.getOrigin(), Object.class);
                if (obj == null) return null;

                return handleReturn(obj, declaredMethod);
            }
        };
    }

    private WrapSupplier<Object> handleMethodWrapping(Object origin, Wrapping wrapping, AdvancedClass<?> clazz, Method declaredMethod) {
        final String key = getKeyForMapping(wrapping);
        final String name = getProperNameForVersion(declaredMethod, wrapping.value());

        final String[] rawTypes = wrapping.paramTypes();
        final Class<?>[] paramTypes = new Class<?>[rawTypes.length];

        for (int i = 0; i < rawTypes.length; i++) {
            String raw = rawTypes[i];
            paramTypes[i] = ClassHelper.parse(raw);
        }
        clazz.preDiscoverMethod(key, name, paramTypes);

        return new WrapSupplier<>(origin) {
            @Override
            public Object supply(Object[] params) {
                Object obj = clazz.executeMethod(key, getOrigin(), Object.class, params);
                return handleReturn(obj, declaredMethod);
            }
        };
    }

    private Object handleReturn(Object obj, Method declaredMethod) {
        if (obj == null) return null;
        if (obj.getClass().isAssignableFrom(declaredMethod.getReturnType())) {
            return declaredMethod.getReturnType().cast(obj);
        }

        return (declaredMethod.getReturnType().isAnnotationPresent(NMSWrap.class) || declaredMethod.getReturnType().isAnnotationPresent(CraftWrap.class))
                ? wrapFromOrigin(obj, declaredMethod.getReturnType()) : obj;
    }
}
