package net.iceyleagons.icicle.utils;

import java.util.Map;

public final class Defaults {

    public static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;

    static {
        DEFAULT_TYPE_VALUES = Map.of(
                boolean.class, false,
                byte.class, (byte) 0,
                short.class, (short) 0,
                int.class, 0,
                long.class, 0L,
                float.class, 0f);
    }
}
