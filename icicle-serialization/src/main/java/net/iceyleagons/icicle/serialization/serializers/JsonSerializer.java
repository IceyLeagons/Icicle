package net.iceyleagons.icicle.serialization.serializers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.serialization.AbstractSerializer;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JsonSerializer extends AbstractSerializer {

    private boolean prettyPrint;

    @Override
    protected String serializeToString(ObjectDescriptor objectDescriptor) {
        return buildFromDescriptor(objectDescriptor).toString(prettyPrint ? 2 : 0);
    }

    @Override
    protected ObjectDescriptor getFromString(String string, Class<?> type) {
        return fromJSON(new JSONObject(string), type);
    }

    private static ObjectDescriptor fromJSON(JSONObject jsonObject, Class<?> type) {
        ObjectDescriptor objectDescriptor = new ObjectDescriptor(type);

        Tuple<Field[], Field[]> fields = ObjectMapper.getFields(type);
        Field[] valueArray = fields.getA();
        Field[] subObjectArray = fields.getB();

        for (Field field : valueArray) {
            String key = ObjectMapper.getFieldKey(field);

            if (!jsonObject.has(key)) {
                throw new IllegalStateException("No field with key " + key + " found!");
            }

            objectDescriptor.getValueFields().add(ObjectMapper.getFieldWithValue(field, jsonObject.get(key)));
        }

        for (Field field : subObjectArray) {
            String key = ObjectMapper.getFieldKey(field);

            if (!jsonObject.has(key)) {
                throw new IllegalStateException("No field with key " + key + " found!");
            }

            Object value = jsonObject.get(key);

            if (value instanceof JSONArray) {
                List<ObjectDescriptor> descriptors = new ArrayList<>();
                JSONArray array = (JSONArray) value;

                for (Object o : array) {
                    if (o instanceof JSONObject) {
                        descriptors.add(fromJSON((JSONObject) o, field.getType()));
                    }
                }

                objectDescriptor.getSubObjectArrays().add(ObjectMapper.getFieldWithValue(field, descriptors));
                continue;
            }

            if (value instanceof JSONObject) {
                objectDescriptor.getSubObjects().add(ObjectMapper.getFieldWithValue(field, fromJSON((JSONObject) value, field.getType())));
            }
        }

        return objectDescriptor;
    }

    private static JSONObject buildFromDescriptor(ObjectDescriptor objectDescriptor) {
        JSONObject jsonObject = new JSONObject();

        // Handling default Java types (String, int, long, etc.)
        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            String key = valueField.getA();
            Object value = valueField.getC();

            jsonObject.put(key, value);
        }

        // Handling sub-object non-array types
        for (Triple<String, Field, ObjectDescriptor> subObject : objectDescriptor.getSubObjects()) {
            String key = subObject.getA();
            ObjectDescriptor subDescriptor = subObject.getC();

            jsonObject.put(key, buildFromDescriptor(subDescriptor));
        }

        // Handling sub-object array types
        for (Triple<String, Field, List<ObjectDescriptor>> subObjectArray : objectDescriptor.getSubObjectArrays()) {
            JSONArray jsonArray = new JSONArray();
            String key = subObjectArray.getA();

            for (ObjectDescriptor descriptor : subObjectArray.getC()) {
                jsonArray.put(buildFromDescriptor(descriptor));
            }

            jsonObject.put(key, jsonArray);
        }

        return jsonObject;
    }
}
