package net.iceyleagons.icicle.utilities;

import java.lang.reflect.Array;

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
}
