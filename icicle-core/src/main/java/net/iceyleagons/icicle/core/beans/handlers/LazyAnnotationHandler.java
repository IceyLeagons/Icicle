/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.beans.handlers;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.bean.Lazy;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.QualifierKey;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import net.iceyleagons.icicle.core.proxy.interceptor.LazyMethodDelegation;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.utilities.Defaults;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Set;

/**
 * This manages the autowiring of parameters marked with @{@link Lazy}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 18, 2022
 * @see Lazy
 */
@AnnotationHandler
@RequiredArgsConstructor
public class LazyAnnotationHandler implements AutowiringAnnotationHandler {

    private final BeanManager beanManager;
    private final ByteBuddy byteBuddy = ByteBuddyProxyHandler.getNewByteBuddyInstance();
    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(Lazy.class);
    }

    @Override
    public <T> @Nullable T getValueForAnnotation(Annotation annotation, Class<T> wantedType, Parameter parameter) {
        try {
            return getProxy(wantedType, new QualifierKey(wantedType, QualifierKey.getQualifier(parameter)));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Could not create @Lazy proxy due to missing empty public constructor.");
        } catch (Exception e) {
            throw new IllegalStateException("Could not create @Lazy proxy due to an exception.", e);
        }
    }

    /**
     * Creates the temporary implementation proxy for the given type and qualifier.
     *
     * @param type the type class
     * @param qualifierKey the qualifier
     * @return the created proxy instance
     * @param <T> the type
     * @throws NoSuchMethodException if the proxy cannot be created due to an underlying {@link NoSuchMethodException}
     * @throws InvocationTargetException if the proxy cannot be created due to an underlying {@link InvocationTargetException}
     * @throws InstantiationException if the proxy cannot be created due to an underlying {@link InstantiationException}
     * @throws IllegalAccessException if the proxy cannot be created due to an underlying {@link IllegalAccessException}
     */
    private <T> T getProxy(Class<T> type, QualifierKey qualifierKey) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // TODO At the moment fields are not counted for!!
        Constructor<T> constructor = BeanUtils.getResolvableConstructor(type);

        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] dumbParams = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            dumbParams[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(paramTypes[i], null);
        }

        return byteBuddy
                .with(AnnotationRetention.ENABLED)
                .subclass(type)
                .method(ElementMatchers.any()).intercept(MethodDelegation.to(new LazyMethodDelegation(this.beanManager, qualifierKey)))
                .make().load(type.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())

                // We want an empty constructor, as the reason we could not initialize the class is because of constructor parameter issues.
                // A proper constructor creation will happen via the delegation
                .getLoaded().getDeclaredConstructor(paramTypes)
                .newInstance(dumbParams);

    }
}
