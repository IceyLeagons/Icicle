package net.iceyleagons.icicle.utilities;

import net.iceyleagons.icicle.utilities.generic.GenericUtils;

/**
 * Utility class for arrayss.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 06, 2021
 */
public final class ArrayUtils {

    @SuppressWarnings("unchecked")
    public static <T> T[] appendToArray(T[] list, T... toAppend) {
        final T[] result = (T[]) GenericUtils.createGenericArray(list.getClass().getComponentType(), list.length + toAppend.length);

        System.arraycopy(list, 0, result, 0, list.length);
        System.arraycopy(toAppend, 0, result, list.length, toAppend.length);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] extendArray(T[] source, int additionalSpace) {
        if (additionalSpace == 0) return source;

        final T[] result = (T[]) GenericUtils.createGenericArray(source.getClass().getComponentType(), source.length + additionalSpace);
        System.arraycopy(source, 0, result, 0, source.length);

        return result;
    }
}
