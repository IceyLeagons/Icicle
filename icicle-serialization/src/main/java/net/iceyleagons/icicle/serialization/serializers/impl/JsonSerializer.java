/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.ObjectValue;
import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.SerializedObject;
import net.iceyleagons.icicle.serialization.serializers.FileSerializer;
import net.iceyleagons.icicle.serialization.serializers.StringSerializer;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 26, 2022
 */
public class JsonSerializer implements FileSerializer, StringSerializer {

    private final ObjectMapper objectMapper;
    private final boolean pretty;

    public JsonSerializer() {
        this(false);
    }

    public JsonSerializer(boolean pretty) {
        this.pretty = pretty;
        this.objectMapper = new ObjectMapper();
    }


    public JSONObject serialize(Object object) {
        return serialize(object, new JSONObject());
    }

    private JSONObject serialize(Object object, JSONObject root) {
        SerializedObject obj = objectMapper.mapObject(object);
        return serialize(obj, root);
    }

    private JSONObject serialize(SerializedObject serializedObject, JSONObject root) {
        for (ObjectValue value : serializedObject.getValues()) {
            if (value.shouldConvert()) {
                Class<?> vClass = value.getValue().getClass();
                root.put(value.getKey(), SerializationUtils.isSubObject(vClass) ? serialize((SerializedObject) value.getValue(), new JSONObject()) : value.getValue());
            } else if (value.isValuePrimitiveOrString() || value.isEnum()) {
                root.put(value.getKey(), value.getValue());
            } else if (value.isArray() || value.isCollection()) {
                JSONArray jsonArray = toJsonArray(value.getValue());
                root.put(value.getKey(), jsonArray);
            } else if (value.isMap()) {
                Object array = value.getValue();
                root.put(value.getKey(), mapMap(array));
            } else if (value.isSubObject()) {
                root.put(value.getKey(), serialize((SerializedObject) value.getValue(), new JSONObject()));
            }
        }

        return root;
    }

    public <T> T deserializeObject(JSONObject jsonObject, Class<T> type) {
        return objectMapper.demapObject(deserialize(jsonObject, type), type);
    }

    private SerializedObject deserialize(JSONObject json, Class<?> javaType) {
        Set<ObjectValue> values = SerializationUtils.getValuesForClass(javaType, (field, key) -> {
            if (SerializationUtils.shouldConvert(field)) {
                Object obj = json.get(key);
                if (obj instanceof JSONObject) {
                    return deserialize((JSONObject) obj, field.getType());
                }
                return json.get(key);
            } else if (SerializationUtils.isValuePrimitiveOrString(field.getType()) || SerializationUtils.isEnum(field.getType())) {
                return json.get(key);
            } else if (SerializationUtils.isArray(field.getType())) {
                return fromJsonArray(json.getJSONArray(key), field.getType().getComponentType());
            } else if (SerializationUtils.isCollection(field.getType())) {
                return fromJsonArray(json.getJSONArray(key), GenericUtils.getGenericTypeClass(field, 0));
            } else if (SerializationUtils.isMap(field.getType())) {
                return demapMap(json.getJSONObject(key), GenericUtils.getGenericTypeClass(field, 1)); // TODO
            } else if (SerializationUtils.isSubObject(field.getType())) {
                return deserialize(json.getJSONObject(key), field.getType());
            }

            return null;
        });

        SerializedObject obj = new SerializedObject(javaType);
        obj.getValues().addAll(values);

        return obj;
    }

    private JSONObject mapMap(Object array) {
        JSONObject obj = new JSONObject();

        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object entryKey = entry.getKey();
                Object entryValue = entry.getValue();

                Object val = SerializationUtils.isSubObject(entryValue.getClass()) ? serialize((SerializedObject) entryValue, new JSONObject()) : entryValue;
                obj.put(entryKey.toString(), val);
            }
        }

        return obj;
    }

    private Object demapMap(JSONObject object, Class<?> valueType) {
        Object array = GenericUtils.createGenericArrayWithoutCasting(Map.Entry.class, object.length());

        int i = 0;
        for (Map.Entry<String, Object> entry : object.toMap().entrySet()) {
            Object entryValue = entry.getValue();
            Object value = SerializationUtils.isSubObject(valueType) ? deserialize((JSONObject) entryValue, valueType) : entryValue;

            Array.set(array, i++, Map.entry(entry.getKey(), value));
        }

        return array;
    }

    private Object fromJsonArray(JSONArray array, Class<?> javaType) {
        Object obj = GenericUtils.createGenericArrayWithoutCasting(javaType, array.length());
        for (int i = 0; i < array.length(); i++) {
            Object o = array.get(i);

            if (o instanceof JSONObject) {
                Array.set(obj, i, deserialize((JSONObject) o, javaType));
                continue;
            }

            Array.set(obj, i, o);
        }

        return obj;
    }

    private JSONArray toJsonArray(Object array) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);

            if (o instanceof SerializedObject) {
                jsonArray.put(serialize((SerializedObject) o, new JSONObject()));
                continue;
            }

            jsonArray.put(o);
        }
        return jsonArray;
    }

    @Override
    @SneakyThrows
    public void serializeToPath(Object object, Path path) {
        try (Writer writer = Files.newBufferedWriter(path, StandardOpenOption.WRITE)) {
            serialize(object).write(writer, pretty ? 2 : 0, pretty ? 4 : 0);
        }
    }

    @Override
    @SneakyThrows
    public <T> T deserializeFromPath(Path path, Class<T> type) {
        try (Reader reader = Files.newBufferedReader(path)) {
            return deserializeObject(new JSONObject(reader), type);
        }
    }

    @Override
    public String serializeToString(Object object) {
        return serialize(object).toString(pretty ? 2 : 0);
    }

    @Override
    public <T> T deserializeFromString(String string, Class<T> type) {
        return deserializeObject(new JSONObject(string), type);
    }
}
