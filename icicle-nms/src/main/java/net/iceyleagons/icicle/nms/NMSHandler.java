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

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.nms.annotations.Wrapping;
import net.iceyleagons.icicle.nms.utils.ClassHelper;
import net.iceyleagons.icicle.utilities.AdvancedClass;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static net.iceyleagons.icicle.nms.NMSHelper.getKeyForMapping;
import static net.iceyleagons.icicle.nms.NMSHelper.getWrapClass;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 27, 2021
 */
public class NMSHandler {

    private final Application application;
    private final ByteBuddy byteBuddy;

    public NMSHandler(Application application) {
        this.application = application;
        this.byteBuddy = application.getBeanManager().getProxyHandler().getProxy();
    }

    @SneakyThrows
    public <T> T wrap(Object origin, Class<T> toWrap) {
        if (toWrap == null || !toWrap.isInterface()) {
            throw new IllegalArgumentException("toWrap must be an interface!");
        }

        AdvancedClass<?> clazz = getWrapClass(toWrap);
        if (clazz == null) {
            throw new IllegalStateException("Could not create wrap for: "  + toWrap.getName() + " , because the to-be-wrapped class is not found!");
        }

        Map<Method, WrapSupplier<Object>> suppliers = new HashMap<>();

        for (Method declaredMethod : toWrap.getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Wrapping.class)) continue;
            Wrapping wrapping = declaredMethod.getAnnotation(Wrapping.class);

            if (wrapping.isField()) {
                suppliers.put(declaredMethod, (params) -> {
                    Object obj = clazz.getFieldValue(wrapping.value(), origin, Object.class);

                    if (obj.getClass().isAssignableFrom(declaredMethod.getReturnType())) {
                        return declaredMethod.getReturnType().cast(obj);
                    }

                    return wrap(obj, declaredMethod.getReturnType());
                });
                continue;
            }

            String key = getKeyForMapping(wrapping);

            String[] rawTypes = wrapping.paramTypes();
            Class<?>[] paramTypes = new Class<?>[rawTypes.length];

            for (int i = 0; i < rawTypes.length; i++) {
                String raw = rawTypes[i];
                paramTypes[i] = ClassHelper.parse(raw);
            }

            clazz.preDiscoverMethod(key, wrapping.value(), paramTypes);

            suppliers.put(declaredMethod, (params) -> {
                Object obj = clazz.executeMethod(key, origin, Object.class, params);
                if (obj.getClass().isAssignableFrom(declaredMethod.getReturnType())) {
                    return declaredMethod.getReturnType().cast(obj);
                }

                return wrap(obj, declaredMethod.getReturnType());
            });
        }

        return createObject(toWrap, suppliers);
    }



    private <T> T createObject(Class<T> clazz, Map<Method, WrapSupplier<Object>> suppliers) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        DynamicType.Builder<T> builder = byteBuddy.subclass(clazz);
        for (Map.Entry<Method, WrapSupplier<Object>> methodWrapSupplierEntry : suppliers.entrySet()) {
            Method method = methodWrapSupplierEntry.getKey();
            WrapSupplier<Object> supplier = methodWrapSupplierEntry.getValue();

            if (method.getAnnotation(Wrapping.class).isField()) {
                builder = builder.define(method).intercept(FixedValue.value(supplier.supply(null)));
                continue;
            }

            builder = builder.define(method).intercept(MethodDelegation.to(new MethodDelegator(supplier)));
        }

        return builder.make()
                .load(clazz.getDeclaringClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded().getDeclaredConstructor().newInstance();
    }

    @RequiredArgsConstructor
    static class MethodDelegator {
        private final WrapSupplier<Object> supplier;

        @RuntimeType
        Object foo(@Advice.AllArguments Object[] args) {
            return supplier.supply(args);
        }
    }
}
