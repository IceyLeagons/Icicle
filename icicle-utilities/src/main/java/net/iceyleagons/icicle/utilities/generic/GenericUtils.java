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

package net.iceyleagons.icicle.utilities.generic;

import com.google.common.reflect.TypeToken;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.*;
import java.util.Objects;

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

    public static Object createGenericArrayWithoutCasting(Class<?> type, int size) {
        return Array.newInstance(type, size);
    }

    @Nullable
    public static Type getGenericType(Class<?> from, int typeIndex) {
        return ((ParameterizedType) from.getGenericSuperclass()).getActualTypeArguments()[typeIndex];
    }

    @Nullable
    public static Type getGenericType(Method from, int typeIndex) {
        return ((ParameterizedType) from.getGenericReturnType()).getActualTypeArguments()[typeIndex];
    }

    @Nullable
    public static Type getGenericType(Field from, int typeIndex) {
        return ((ParameterizedType) from.getGenericType()).getActualTypeArguments()[typeIndex];
    }

    public static Class<?> getGenericTypeClass(Method from, int typeIndex) {
        return TypeToken.of(Objects.requireNonNull(getGenericType(from, typeIndex))).getRawType();
    }

    public static Class<?> getGenericTypeClass(Field field, int typeIndex) {
        return TypeToken.of(Objects.requireNonNull(getGenericType(field, typeIndex))).getRawType();
    }

    public static Class<?> getGenericTypeClass(Class<?> from, int typeIndex) {
        return TypeToken.of(Objects.requireNonNull(getGenericType(from, typeIndex))).getRawType();
    }

    public static <T> T[] genericArrayToNormalArray(Object genericArray, Class<T> wantedType) {
        int originalSize = Array.getLength(genericArray);
        T[] cloneArray = createGenericArray(wantedType, originalSize);

        for (int i = 0; i < originalSize; i++) {
            Object element = Array.get(genericArray, i);

            if (!wantedType.isInstance(element))
                throw new IllegalStateException("Generic array element type ( " + element.getClass().getName() + " ) cannot be casted to " + wantedType.getName());

            cloneArray[i] = wantedType.cast(element);
        }

        return cloneArray;
    }
}
