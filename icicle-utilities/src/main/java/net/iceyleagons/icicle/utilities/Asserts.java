package net.iceyleagons.icicle.utilities;

/**
 * Utility methods for checking input.
 *
 * @version 1.0.0
 * @since Aug. 23, 2021
 * @author TOTHTOMI
 */
public class Asserts {

    /**
     * Checks whether the supplied object is null, and if it is, an {@link IllegalStateException} will be thrown
     * with the supplied message.
     *
     * @param object the object to check
     * @param msg the error message
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
     * @param msg the error message
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
     * @param msg the error message
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
     *
     * Basically the same as {@link #state(boolean, String)}, but this throws an
     * {@link IllegalArgumentException} rather than an {@link IllegalStateException}
     *
     * @param expression the expression to check
     * @param msg the error message
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
     *
     * Basically the same as {@link #isTrue(boolean, String)}, but this throws an
     * {@link IllegalStateException} rather than an {@link IllegalArgumentException}
     *
     * @param expression the expression to check
     * @param msg the error message
     * @throws IllegalStateException if the expression is false
     */
    public static void state(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalStateException(msg);
        }
    }
}
