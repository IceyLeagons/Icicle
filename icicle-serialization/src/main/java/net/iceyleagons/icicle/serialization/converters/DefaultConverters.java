package net.iceyleagons.icicle.serialization.converters;

import net.iceyleagons.icicle.serialization.converters.builtin.JSONToStringConverter;
import net.iceyleagons.icicle.serialization.converters.builtin.UUIDToStringConverter;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class DefaultConverters {

    public static final Map<Class<?>, ValueConverter<?,?>> converters = new HashMap<>();

    static {
        converters.put(UUID.class, new UUIDToStringConverter());
        converters.put(JSONObject.class, new JSONToStringConverter());
    }
}
