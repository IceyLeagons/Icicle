package net.iceyleagons.icicle.serialization.serializers;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.MappedObject;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.ObjectValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public class JsonSerializer extends ObjectMapper {

    private final int identFactor;

    public JsonSerializer(int identFactor) {
        super();
        this.identFactor = identFactor;
    }

    @SneakyThrows
    public <T> T convertFromString(String input, Class<T> wantedType) {
        JSONObject jsonObject = new JSONObject(input);
        MappedObject mappedObject = new MappedObject(wantedType);

        fromJson(jsonObject, mappedObject, wantedType);

        return super.demapObject(mappedObject, wantedType);
    }

    public String convertToString(Object object) {
        MappedObject mapped = super.mapObject(object);
        JSONObject root = new JSONObject();
        toJson(mapped, root);

        return root.toString(identFactor);
    }

    private void fromJson(JSONObject value, MappedObject root, Class<?> javaType) {
        for (Field declaredField : javaType.getDeclaredFields()) {
            if (shouldIgnore(declaredField)) continue;
            Class<?> fieldType = declaredField.getType();
            String key = ObjectMapper.getName(declaredField);

            if (ObjectValue.isValuePrimitiveOrString(fieldType)) {
                root.addValue(new ObjectValue(fieldType, declaredField, value.get(key)));
            } else if (ObjectValue.isCollection(fieldType) || ObjectValue.isArray(fieldType)) {
                JSONArray jsonArray = value.getJSONArray(key);
                Object[] array = new Object[jsonArray.length()];

                int i = 0;
                for (Object o : jsonArray) {
                    if (o instanceof JSONObject) {
                        MappedObject mapped = new MappedObject(fieldType);
                        fromJson((JSONObject) o, mapped, fieldType.arrayType());

                        array[i++] = mapped;
                        continue;
                    }

                    array[i++] = o;
                }

                root.addValue(new ObjectValue(fieldType.arrayType(), declaredField, array));
            } else if (ObjectValue.isMap(fieldType)) {
                //TODO figure out
            } else if (ObjectValue.isSubObject(fieldType)) {
                JSONObject obj = value.getJSONObject(key);
                MappedObject mapped = new MappedObject(fieldType);

                fromJson(obj, mapped, fieldType);

                root.addValue(new ObjectValue(fieldType, declaredField, mapped));
            } else {
                throw new IllegalStateException("Unsupported value type: " + fieldType.getName());
            }
        }
    }

    private void toJson(MappedObject mapped, JSONObject root) {
        for (ObjectValue ov : mapped.getValues()) {
            toJson(ov, root);
        }
    }

    private void toJson(ObjectValue value, JSONObject root) {
        if (value.isValuePrimitiveOrString()) {
            root.put(value.getKey(), value.getValue());
        } else if (value.isArray() || value.isCollection()) {
            //We test for collection, because when mapping we convert collections into arrays for easier work later on
            Object array = value.getValue();
            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < Array.getLength(array); i++) {
                Object o = Array.get(array, i);

                if (o instanceof MappedObject) {
                    JSONObject mojson = new JSONObject();
                    MappedObject mo = (MappedObject) o;

                    toJson(mo, mojson);
                    jsonArray.put(mojson);

                    continue;
                }

                jsonArray.put(o);
            }
            root.put(value.getKey(), jsonArray);
        } else if (value.isMap()) {
            Object array = value.getValue();
            JSONObject obj = new JSONObject();

            for (int i = 0; i < Array.getLength(array); i++) {
                Object o = Array.get(array, i);
                if (o instanceof Map.Entry) {
                    Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                    Object entryKey = entry.getKey();
                    Object entryValue = entry.getValue();

                    Object val;
                    if (ObjectValue.isValuePrimitiveOrString(entryValue.getClass())) {
                        val = entryValue;
                    } else {
                        val = new JSONObject();
                        toJson((MappedObject) entryValue, (JSONObject) val);
                    }


                    obj.put(entryKey.toString(), val);
                }
            }
            root.put(value.getKey(), obj);
        } else if (value.isSubObject()) {
            JSONObject sub = new JSONObject();
            toJson((MappedObject) value.getValue(), sub);
            root.put(value.getKey(), sub);
        } else {
            throw new IllegalStateException("Unsupported value type: " + value.getJavaType().getName());
        }
    }
}
