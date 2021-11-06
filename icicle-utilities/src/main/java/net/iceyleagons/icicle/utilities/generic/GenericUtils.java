package net.iceyleagons.icicle.utilities.generic;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Utility methods to make dealing with generic types easier!
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 28, 2021
 */
public final class GenericUtils {

    @SuppressWarnings("unchecked")
    public static <T> T[] createGenericArray(Class<T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }

    @Nullable
    public static Type getGenericType(Class<?> from, int typeIndex) {
        return ((ParameterizedType) from.getGenericSuperclass()).getActualTypeArguments()[typeIndex];
    }

    @Nullable
    public static Type getGenericType(Field from, int typeIndex) {
        return ((ParameterizedType) from.getGenericType()).getActualTypeArguments()[typeIndex];
    }

    @Nullable
    public static Class<?> getGenericTypeClass(Field field, int typeIndex) {
        return TypeToken.of(getGenericType(field, typeIndex)).getRawType();
    }

    @Nullable
    public static Class<?> getGenericTypeClass(Class<?> from, int typeIndex) {
        return TypeToken.of(getGenericType(from, typeIndex)).getRawType();
    }
}
