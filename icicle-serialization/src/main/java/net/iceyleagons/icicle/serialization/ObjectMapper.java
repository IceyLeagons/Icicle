package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.annotations.SerializeIgnore;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.utilities.GenericUtils;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import org.json.JSONObject;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.iceyleagons.icicle.serialization.converter.ConverterAutoCreateAnnotationHandler.converters;
import static net.iceyleagons.icicle.utilities.StringUtils.containsIgnoresCase;

public class ObjectMapper {

    public static Map<String, Object> mapObject(Object object) {
        //Warning! probably ugly code ahead, however it "hopefully" it works fine, all edge-cases "should" be implemented.
        //The good thing i that it runs 5ms avg.

        Map<String, Object> map = new HashMap<>();

        for (Field declaredField : object.getClass().getDeclaredFields()) {
            if (shouldIgnore(declaredField)) continue;

            Class<?> type = declaredField.getType();
            String name = declaredField.isAnnotationPresent(SerializedName.class) ? declaredField.getAnnotation(SerializedName.class).value() : declaredField.getName();
            Object value = ReflectionUtils.get(declaredField, object, Object.class);

            if (value == null) {
                map.put(name, null);
                continue;
            }

            if (value instanceof JSONObject) {
                map.put(name, ((JSONObject) value).toMap());
                continue;
            }

            Type genericType = declaredField.getGenericType();
            if (converters.containsKey(genericType)) {
                Object obj = converters.get(genericType).convertToStorageAttributeFromObject(value);

                if (obj instanceof JSONObject) {
                    map.put(name, ((JSONObject) obj).toMap());
                    continue;
                }

                if (isSubObject(obj.getClass())) {
                    Map<String, Object> mappedConvertedSubObject = mapObject(obj);
                    map.put(name, mappedConvertedSubObject);
                    continue;
                }

                map.put(name, obj);
            }

            if (isSubObject(type)) {
                if (type.isArray()) {
                    List<Map<String, Object>> mapArray = new ArrayList<>();

                    for (int i = 0; i < Array.getLength(value); i++) {
                        Map<String, Object> mappedConvertedSubObject = mapObject(Array.get(value, i));
                        mapArray.add(mappedConvertedSubObject);
                    }

                    map.put(name, mapArray);
                    continue;
                }

                Map<String, Object> mappedSubObject = mapObject(value);
                map.put(name, mappedSubObject);
                continue;
            }


            map.put(name, value);
        }

        return map;
    }

    @SuppressWarnings("unchecked")
    public static <T> T toObject(Map<String, Object> values, Class<T> type) {
        T instance = generateInstance(type);
        Map<String, Field> fields = getFields(type);

        for (Map.Entry<String, Object> stringObjectEntry : values.entrySet()) {
            String key = stringObjectEntry.getKey();
            Object value = stringObjectEntry.getValue();

            Field field = fields.get(key);
            Class<?> fieldType = field.getType();

            if (value instanceof JSONObject) {
                JSONObject jsonObject = (JSONObject) value;
                Object obj = toObject(jsonObject.toMap(), fieldType);
                ReflectionUtils.set(field, instance, obj);
                continue;
            }

            Type genericType = field.getGenericType();
            if (converters.containsKey(genericType)) {

                Object obj = null;
                if (value instanceof JSONObject) {
                    JSONObject jsonObject = (JSONObject) value;
                    Object subObject = toObject(jsonObject.toMap(), fieldType);
                    obj = converters.get(genericType).convertToEntityAttributeFromObject(subObject);
                }

                if (isSubObject(fieldType)) {
                    Object subObject = toObject((Map<String, Object>) value, fieldType);
                    obj = converters.get(genericType).convertToEntityAttributeFromObject(subObject);
                }

                ReflectionUtils.set(field, instance, obj);
            }

            if (isSubObject(fieldType)) {
                if (fieldType.isArray()) {
                    List<Map<String, Object>> mapArray = (List<Map<String, Object>>) value;
                    Object[] array = GenericUtils.createGenericArray(fieldType.getComponentType(), mapArray.size());

                    for (int i = 0; i < mapArray.size(); i++) {
                        Map<String, Object> map = mapArray.get(i);
                        array[i] = toObject(map, fieldType.getComponentType());
                    }

                    ReflectionUtils.set(field, instance, array);
                    continue;
                }

                Object subObject = toObject((Map<String, Object>) value, fieldType);
                ReflectionUtils.set(field, instance, subObject);
                continue;
            }

            ReflectionUtils.set(field, instance, value);
        }

        return instance;
    }

    private static Map<String, Field> getFields(Class<?> clazz) {
        Map<String, Field> fields = new HashMap<>();

        for (Field declaredField : clazz.getDeclaredFields()) {
            if (shouldIgnore(declaredField)) continue;
            String name = declaredField.isAnnotationPresent(SerializedName.class) ? declaredField.getAnnotation(SerializedName.class).value() : declaredField.getName();

            fields.put(name, declaredField);
        }

        return fields;
    }

    private static <T> T generateInstance(Class<T> type) throws IllegalStateException {
        try {
            Constructor<T>  constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Object must contain one public empty constructor!", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Could not instantiate object!");
        }
    }

    public static boolean isSubObject(Class<?> type) {
        String typeName = type.getTypeName();
        return !containsIgnoresCase(typeName, "string") &&
                !containsIgnoresCase(typeName, "int") &&
                !containsIgnoresCase(typeName, "boolean") &&
                !containsIgnoresCase(typeName, "long") &&
                !containsIgnoresCase(typeName, "float") &&
                !containsIgnoresCase(typeName, "double") &&
                !containsIgnoresCase(typeName, "short") &&
                !containsIgnoresCase(typeName, "byte") &&
                !containsIgnoresCase(typeName, "char");
    }

    private static boolean shouldIgnore(Field field) {
        return Modifier.isTransient(field.getModifiers()) && field.isAnnotationPresent(SerializeIgnore.class);
    }
}
