package net.iceyleagons.icicle.utilities.generic;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods to make dealing with generic types easier!
 *
 * @version 1.0.0
 * @since Aug. 28, 2021
 * @author TOTHTOMI
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
    public static Class<?> getGenericTypeClass(Class<?> from, int typeIndex) {
        return TypeToken.of(getGenericType(from, typeIndex)).getRawType();
    }
}
