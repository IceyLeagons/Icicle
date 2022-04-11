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

package net.iceyleagons.icicle.core.utils;

import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.annotations.PostConstruct;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.utilities.Asserts;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Utility methods for the creating and autowiring of beans.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public final class BeanUtils {

    /**
     * Scans all classes available to the provided {@link org.reflections.Reflections}, and returns all that implement the provided class.
     *
     * @param interfaceClass the class which should be implemented to be returned.
     * @param reflections    the reflections object with the packages fed into.
     * @return a list of the classes that implement said interface.
     */
    public static List<Class<?>> getImplementationsOfInterface(Class<?> interfaceClass, Reflections reflections) {
        if (!interfaceClass.isInterface())
            throw new IllegalArgumentException("Non-interface class passed to interfaceClass argument.");
        List<Class<?>> set = new ArrayList<>();
        Stack<Class<?>> stack = new Stack<>();
        stack.push(interfaceClass);

        while (!stack.isEmpty()) {
            Class<?> interface0 = stack.pop();
            if (interface0.isInterface()) {
                for (Class<?> aClass : reflections.getSubTypesOf(interface0)) {
                    if (aClass.isInterface()) {
                        stack.push(aClass);
                        continue;
                    }

                    set.add(aClass);
                }
            } else {
                set.add(interface0);
            }
        }

        return set;
    }

    public static void invokePostConstructor(Class<?> clazz, Object parent) {
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(PostConstruct.class))
                .forEach(m -> {
                    try {
                        m.invoke(parent);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                });
    }

    /**
     * Instantiates a class using the supplied constructor and arguments.
     * If a {@link BeanProxyHandler} is present and not null, the object will be created via the proxy and not {@link Constructor#newInstance(Object...)}
     *
     * @param constructor      the constructor to use (from {@link #getResolvableConstructor(Class)})
     * @param beanProxyHandler the {@link BeanProxyHandler} to use (can be null)
     * @param arguments        the constructor parameters
     * @param <T>              the type
     * @return the created bean
     * @throws BeanCreationException if any exception happens during the instantiation
     */
    public static <T> T instantiateClass(Constructor<T> constructor, @Nullable BeanProxyHandler beanProxyHandler, Object... arguments) throws BeanCreationException {
        Asserts.notNull(constructor, "Constructor must not be null!");

        try {
            constructor.setAccessible(true);


            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Asserts.isTrue(arguments.length <= parameterTypes.length, "Cannot specify more arguments than constructor parameters!");

            Object[] argObjects = new Object[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                if (arguments[i] == null) {
                    Class<?> paramType = parameterTypes[i];
                    argObjects[i] = paramType.isPrimitive() ? Defaults.DEFAULT_TYPE_VALUES.get(paramType) : null;
                    continue;
                }
                argObjects[i] = arguments[i];
            }

            if (Kotlin.isKotlinReflectionPresent() && Kotlin.isKotlinType(constructor.getDeclaringClass())) {
                //TODO are kotlin classes supported by ByteBuddy? (collectively: figure out how to enhance kotlin classes)
                return Kotlin.instantiateKotlinClass(constructor, argObjects);
            }

            T value = beanProxyHandler == null ? constructor.newInstance(argObjects) : beanProxyHandler.createEnhancedBean(constructor, argObjects);
            if (value == null)
                throw new BeanCreationException(constructor, "Resulting value from instance generation is null."); //just in case

            return value;
        } catch (InvocationTargetException e) {
            throw new BeanCreationException(constructor, "Constructor execution resulted in an exception.", e);
        } catch (InstantiationException e) {
            throw new BeanCreationException(constructor, "Could not instantiate class. (Is it an abstract class?)");
        } catch (IllegalAccessException e) {
            throw new BeanCreationException(constructor, "Constructor is not accessible! (Is it accessible/public?)");
        }
    }

    /**
     * Returns the resolvable constructor of the class:
     * - if the class only has 1 constructor it will be used
     * - if the class has more than 1 constructors, the one with the least parameters will be used (not implemented, an empty constructor is used currently)
     *
     * @param clazz the class
     * @param <T>   the type of the class
     * @return the constructor
     * @throws IllegalStateException if no public constructors were found
     */
    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getResolvableConstructor(Class<T> clazz) {
        if (Kotlin.isKotlinReflectionPresent() && Kotlin.isKotlinType(clazz)) {
            Constructor<T> kotlinConst = Kotlin.findPrimaryConstructor(clazz);
            if (kotlinConst != null) return kotlinConst;
        }

        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) return (Constructor<T>) constructors[0];
        else if (constructors.length == 0) {
            constructors = clazz.getDeclaredConstructors();
            if (constructors.length == 1) {
                return (Constructor<T>) constructors[0];
            }
        }

        try {
            return clazz.getDeclaredConstructor(); //attempting to grab an empty constructor
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default or single public constructor found for " + clazz);
        }
    }
}
