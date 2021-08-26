package net.iceyleagons.icicle.core.utils;

import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.utilities.Asserts;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class BeanUtils {

    public static <T> T instantiateClass(Class<T> clazz, @Nullable BeanProxyHandler beanProxyHandler) throws BeanCreationException {
        Asserts.notNull(clazz, "Class must not be null!");

        if (clazz.isInterface()) {
            throw new BeanCreationException(clazz, "Specified class is an interface");
        }

        try {
            return instantiateClass(clazz.getDeclaredConstructor(), beanProxyHandler);
        } catch (NoSuchMethodException e) {
            throw new BeanCreationException(clazz, "No default constructor found!", e);
        }
    }

    public static <T> T instantiateClass(Constructor<T> constructor, @Nullable  BeanProxyHandler beanProxyHandler, Object... arguments) throws BeanCreationException{
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

            return beanProxyHandler == null ? constructor.newInstance(argObjects) : beanProxyHandler.createEnhancedBean(constructor, argObjects);
        } catch (InvocationTargetException e) {
            throw new BeanCreationException(constructor, "Constructor execution resulted in an exception.", e);
        } catch (InstantiationException e) {
            throw new BeanCreationException(constructor, "Could not instantiate class. (Is it an abstract class?)");
        } catch (IllegalAccessException e) {
            throw new BeanCreationException(constructor, "Constructor is not accessible! (Is it accessible/public?)");
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> Constructor<T> getResolvableConstructor(Class<T> clazz) {
        Constructor<?>[] constructors = clazz.getConstructors();

        if (constructors.length == 1) return (Constructor<T>) constructors[0];
        try {
            return clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No default or single public constructor found for " + clazz);
        }
    }

    public static <T> T castIfNecessary(Class<T> required, Object object) {
        return required.isInstance(object) ? required.cast(object) : null;
    }
}
