package net.iceyleagons.icicle.core.utils;

import java.util.Map;

/**
 * Utility class to hold all the default values for all the primitive types to be used when autowiring.
 *
 * @version 1.0.0
 * @since Aug. 23, 2021
 * @author TOTHTOMI
 */
public final class Defaults {

    public static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;

    static {
        DEFAULT_TYPE_VALUES = Map.of(
                boolean.class, false,
                byte.class, (byte) 0,
                short.class, (short) 0,
                int.class, 0,
                long.class, 0L,
                float.class, 0f,
                char.class, (char) 0);
    }
}
