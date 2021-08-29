package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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

    @SuppressWarnings("unchecked")
    public static <T> T generateObjectFromData(Class<T> type, Map<String, Object> values) {
        try {
            Constructor<?> emptyConstructor = type.getDeclaredConstructor();
            emptyConstructor.setAccessible(true);

            Object object = emptyConstructor.newInstance();

            for (Field declaredField : type.getDeclaredFields()) {
                Class<?> fieldType = declaredField.getType();
                String name = declaredField.getName(); //TODO or read from annotation if present

                Object mapValue = values.get(name);

                if (mapValue instanceof Map && isSubObject(fieldType)) {
                    ReflectionUtils.set(declaredField, object, generateObjectFromData(fieldType, (Map<String, Object>) mapValue));
                    continue;
                }

                ReflectionUtils.set(declaredField, object, mapValue);
            }

            return ReflectionUtils.castIfNecessary(type, object);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Type must have an empty parameter public constructor!", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Could not create an instance of type.", e);
        }
    }

    public static boolean isSubObject(Class<?> type) {
        return type != String.class && !type.isPrimitive();
    }
}
