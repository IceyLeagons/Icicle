package net.iceyleagons.icicle.utils;

public class Asserts {

    public static void notNull(Object object, String msg) {
        if (object == null) {
            throw new IllegalStateException(msg);
        }
    }

    public static void notEmpty(Object[] array, String msg) {
        notNull(array, "Array must not be null for empty check!");

        if (array.length == 0) {
            throw new IllegalStateException(msg);
        }
    }

    public static void noNullElements(Object[] array, String msg) {
        notNull(array, "Array must not be null for element null check!");

        for (Object o : array) {
            if (o == null) {
                throw new IllegalArgumentException(msg);
            }
        }
    }

    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }

    public static void state(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalStateException(msg);
        }
    }
}
