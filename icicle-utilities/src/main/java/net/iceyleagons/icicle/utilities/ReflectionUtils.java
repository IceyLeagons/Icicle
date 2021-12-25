package net.iceyleagons.icicle.utilities;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class contains utility methods regarding java Reflections.
 * Some methods are only here to catch errors, so the code can be cleaner.
 */
public final class ReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    public static void set(Field field, Object parent, Object value) {
        try {
            field.setAccessible(true);
            field.set(parent, value);
        } catch (Exception e) {
            LOGGER.warn("Could not set field ({}) value inside {}", field.getName(), parent.getClass().getName(), e);
        }
    }

    @Nullable
    public static <T> T get(Field field, Object parent, Class<T> wantedType) {
        try {
            field.setAccessible(true);
            return castIfNecessary(wantedType, field.get(parent));
        } catch (Exception e) {
            LOGGER.warn("Could not get field ({}) value inside {}", field.getName(), parent.getClass().getName(), e);
        }

        return null;
    }

    public static boolean isClassPresent(String className) {
        return isClassPresent(className, ReflectionUtils.class.getClassLoader());
    }

    @Nullable
    public static Field getField(Class<?> parent, String name, boolean setAccessible) {
        try {
            Field field = parent.getDeclaredField(name);
            if (setAccessible) {
                field.setAccessible(true);
            }

            return field;
        } catch (NoSuchFieldException ignored) {
            return null;
        }
    }

    @Nullable
    public static Method getMethod(Class<?> parent, String name, boolean setAccessible, Class<?>... paramTypes) {
        try {
            Method method = parent.getDeclaredMethod(name, paramTypes);
            if (setAccessible) {
                method.setAccessible(true);
            }

            return method;
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    @Nullable
    public static <T> T execute(Method method, Object parent, Class<T> returnType, Object... params) {
        try {
            method.setAccessible(true);
            return castIfNecessary(returnType, method.invoke(parent, params));
        } catch (Exception e) {
            LOGGER.warn("Could not execute method ({}) inside {}", method.getName(), parent.getClass().getName(), e);
        }

        return null;
    }

    public static boolean isClassPresent(String className, ClassLoader classLoader) {
        try {
            Class.forName(className, false, classLoader);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Attempts to cast the supplied object to the required type.
     * If the object is instance of the required type it will get returned,
     * if the object is not instance of the required type, null will be returned.
     *
     * @param required the required type to cast to
     * @param object   the object to cast
     * @param <T>      the type wanted
     * @return the casted object or null
     */
    @Nullable
    public static <T> T castIfNecessary(Class<T> required, Object object) {
        return required.isInstance(object) ? required.cast(object) : null;
    }
}
