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

import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.util.Arrays;

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
    public static <T> T[] ignoreFirst(int toIgnore, T[] source) {
        return Arrays.copyOfRange(source, toIgnore, source.length);
    }

    public static <T> T[] ignoreLast(int toIgnore, T[] source) {
        return Arrays.copyOfRange(source, 0, source.length - toIgnore);
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] extendArray(T[] source, int additionalSpace) {
        return Arrays.copyOf(source, source.length + additionalSpace);
    }
}
