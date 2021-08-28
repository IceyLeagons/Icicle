package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.*;

public class ObjectMapper {

    public static Map<String, Object> mapObject(Object object) {
        Map<String, Object> map = new HashMap<>();

        for (Field declaredField : object.getClass().getDeclaredFields()) {
            String name = declaredField.getName(); //TODO or read from annotation if present
            Object value = ReflectionUtils.get(declaredField, object, Object.class);

            if (isSubObject(declaredField.getType()) && value != null) {
                Map<String, Object> mappedSubObject = mapObject(value);
                map.put(name, mappedSubObject);
                continue;
            }

            map.put(name, value);
        }

        return map;
    }

    private static boolean isSubObject(Class<?> type) {
        return type != String.class && !type.isPrimitive();
    }
}
