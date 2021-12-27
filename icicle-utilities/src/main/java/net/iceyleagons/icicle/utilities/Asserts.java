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

import net.iceyleagons.icicle.utilities.lang.Utility;

/**
 * Utility methods for checking input.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
@Utility
public class Asserts {

    /**
     * Checks whether the supplied object is null, and if it is, an {@link IllegalStateException} will be thrown
     * with the supplied message.
     *
     * @param object the object to check
     * @param msg    the error message
     * @throws IllegalStateException if the object is null
     */
    public static void notNull(Object object, String msg) {
        if (object == null) {
            throw new IllegalStateException(msg);
        }
    }

    /**
     * Checks whether the supplied array is empty, and if it is, an {@link IllegalStateException} will be thrown
     * with the supplied message.
     *
     * @param array the array to check
     * @param msg   the error message
     * @throws IllegalStateException if the array is empty
     */
    public static void notEmpty(Object[] array, String msg) {
        notNull(array, "Array must not be null for empty check!");

        if (array.length == 0) {
            throw new IllegalStateException(msg);
        }
    }

    /**
     * Checks whether the supplied array has any null elements, and if it has, an {@link IllegalStateException} will be thrown
     * with the supplied message.
     *
     * @param array the array to check
     * @param msg   the error message
     * @throws IllegalStateException if the array has any null elements
     */
    public static void noNullElements(Object[] array, String msg) {
        notNull(array, "Array must not be null for element null check!");

        for (Object o : array) {
            if (o == null) {
                throw new IllegalStateException(msg);
            }
        }
    }

    /**
     * Checks whether the supplied expression is false, and if it is, an {@link IllegalArgumentException} will be thrown
     * with the supplied message.
     * <p>
     * Basically the same as {@link #state(boolean, String)}, but this throws an
     * {@link IllegalArgumentException} rather than an {@link IllegalStateException}
     *
     * @param expression the expression to check
     * @param msg        the error message
     * @throws IllegalArgumentException if the expression is false
     */
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks whether the supplied expression is false, and if it is, an {@link IllegalStateException} will be thrown
     * with the supplied message.
     * <p>
     * Basically the same as {@link #isTrue(boolean, String)}, but this throws an
     * {@link IllegalStateException} rather than an {@link IllegalArgumentException}
     *
     * @param expression the expression to check
     * @param msg        the error message
     * @throws IllegalStateException if the expression is false
     */
    public static void state(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalStateException(msg);
        }
    }
}
