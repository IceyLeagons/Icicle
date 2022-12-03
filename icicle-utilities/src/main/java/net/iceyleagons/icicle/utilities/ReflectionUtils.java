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

package net.iceyleagons.icicle.utilities;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This class contains utility methods regarding java Reflections.
 * Some methods are only here to catch errors, so the code can be cleaner.
 *
 * @author TOTHTOMI
 * @version 1.2.0
 */
public final class ReflectionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtils.class);

    public static <T extends Annotation> T getAnnotationOrNull(AccessibleObject obj, Class<T> annClass) {
        return obj.isAnnotationPresent(annClass) ? obj.getAnnotation(annClass) : null;
    }

    public static <T> T invoke(Method method, Object parent, Class<T> wantedType, Object... params) {
        try {
            method.setAccessible(true);
            return castIfNecessary(wantedType, method.invoke(parent, params));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not invoke method (%s) inside %s", method.getName(), parent.getClass().getName()), e);
        }
    }

    public static void set(Field field, Object parent, Object value) {
        try {
            field.setAccessible(true);
            field.set(parent, value);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not set field (%s) value inside %s", field.getName(), parent.getClass().getName()), e);
        }
    }

    @Nullable
    public static <T> T get(Field field, Object parent, Class<T> wantedType) {
        try {
            field.setAccessible(true);
            return castIfNecessary(wantedType, field.get(parent));
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Could not get field (%s) value inside %s", field.getName(), parent.getClass().getName()), e);
        }

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
            throw new IllegalStateException("No field named " + name + " found in " + parent.getName());
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
            throw new IllegalStateException("No method named " + name + " found in " + parent.getName());
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
        if (object != null) {
            Class<?> objType = object.getClass();
            if (objType.equals(String.class)) {
                if (required.equals(Integer.class) || required.equals(int.class)) {
                    return required.cast(Integer.parseInt(object.toString()));
                }

                if (required.equals(Long.class) || required.equals(long.class)) {
                    return required.cast(Long.parseLong(object.toString()));
                }

                if (required.equals(Float.class) || required.equals(float.class)) {
                    return required.cast(Float.parseFloat(object.toString()));
                }

                if (required.equals(Double.class) || required.equals(double.class)) {
                    return required.cast(Double.parseDouble(object.toString()));
                }

                if (required.equals(Short.class) || required.equals(short.class)) {
                    return required.cast(Short.parseShort(object.toString()));
                }
            }
        }

        return required.isInstance(object) ? required.cast(object) : null;
    }
}
