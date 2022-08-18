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

package net.iceyleagons.icicle.serialization.serializers;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.dto.MappedObject;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public class JsonSerializer implements SerializationProvider {

    private static Object convertNullToJson(Object obj) {
        return obj == null ? JSONObject.NULL : obj;
    }

    private static Object convertNullFromJson(Object obj) {
        return obj.equals(JSONObject.NULL) ? null : obj;
    }

    private JSONObject serialize(MappedObject mappedObject, JSONObject root) {
        root.put("dataVersion", mappedObject.getVersion());

        for (ObjectValue value : mappedObject.getValues()) {
            if (value.shouldConvert()) {
                Class<?> vClass = value.getValue().getClass();
                root.put(value.getKey(), SerializationUtils.isSubObject(vClass) ? serialize((MappedObject) value.getValue(), new JSONObject()) : convertNullToJson(value.getValue()));
            } else if (value.isValuePrimitiveOrString() || value.isEnum()) {
                root.put(value.getKey(), convertNullToJson(value.getValue()));
            } else if (value.isArray() || value.isCollection()) {
                JSONArray jsonArray = toJsonArray(value.getValue());
                root.put(value.getKey(), jsonArray);
            } else if (value.isMap()) {
                Object array = value.getValue();
                root.put(value.getKey(), mapMap(array));
            } else if (value.isSubObject()) {
                root.put(value.getKey(), serialize((MappedObject) value.getValue(), new JSONObject()));
            }
        }

        return root;
    }

    private MappedObject deserialize(JSONObject jsonObject, Class<?> javaType) {
        final Set<ObjectValue> values = SerializationUtils.getObjectValues(javaType, jsonObject.getInt("dataVersion"))
                .stream()
                .map(o -> deserializeValue(o, jsonObject, SerializationUtils.getVersion(javaType)))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new MappedObject(javaType, values, jsonObject.getInt("dataVersion"));
    }

    private ObjectValue deserializeValue(ObjectValue objectValue, JSONObject jsonObject, int currentVersion) {
        try {
            if (objectValue.shouldConvert()) {
                Object obj = convertNullFromJson(jsonObject.get(objectValue.getKey()));
                if (obj instanceof JSONObject) {
                    Object result = deserialize((JSONObject) obj, objectValue.getJavaType());
                    return objectValue.copyWithNewValueAndType(result, objectValue.getJavaType());
                }

                return objectValue.copyWithNewValueAndType(obj, objectValue.getJavaType());
            }

            if (objectValue.isValuePrimitiveOrString() || objectValue.isEnum()) {
                return objectValue.copyWithNewValueAndType(convertNullFromJson(jsonObject.get(objectValue.getKey())), objectValue.getJavaType());
            }

            if (objectValue.isArray()) {
                JSONArray jsonArray = jsonObject.getJSONArray(objectValue.getKey());
                return objectValue.copyWithNewValueAndType(
                        fromJsonArray(jsonArray, objectValue.getJavaType().getComponentType()), objectValue.getJavaType()
                );
            }

            if (objectValue.isCollection()) {
                JSONArray jsonArray = jsonObject.getJSONArray(objectValue.getKey());
                return objectValue.copyWithNewValueAndType(
                        fromJsonArray(jsonArray, objectValue.getGenericGetter().getGenericClass(0)), objectValue.getJavaType()
                );
            }

            if (objectValue.isMap()) {
                return objectValue.copyWithNewValueAndType(
                        deMapMap(jsonObject.getJSONObject(objectValue.getKey()), objectValue.getGenericGetter().getGenericClass(1)), objectValue.getJavaType()
                );
            }

            if (objectValue.isSubObject()) {
                Object obj = jsonObject.get(objectValue.getKey());
                if (obj instanceof JSONObject) {
                    return objectValue.copyWithNewValueAndType(deserialize((JSONObject) obj, objectValue.getJavaType()), objectValue.getJavaType());
                }
            }

            return objectValue.copyWithNewValueAndType(jsonObject.get(objectValue.getKey()), objectValue.getJavaType());
        } catch (Exception e) {
            throw new IllegalStateException("Could not parse JSON to ObjectValue. Is the file corrupted?.\n" +
                    "(CV: " + currentVersion + " | PV: " + jsonObject.getInt("dataVersion") + " | FV: " + objectValue.getVersion() + " | K: " + objectValue.getKey() + ")");
        }
    }

    private JSONObject mapMap(Object array) {
        JSONObject obj = new JSONObject();

        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object entryKey = entry.getKey();
                Object entryValue = entry.getValue();

                Object val = SerializationUtils.isSubObject(entryValue.getClass()) ? serialize((MappedObject) entryValue, new JSONObject()) : entryValue;
                obj.put(entryKey.toString(), val);
            }
        }

        return obj;
    }

    private Object deMapMap(JSONObject object, Class<?> valueType) {
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

            if (o instanceof MappedObject) {
                jsonArray.put(serialize((MappedObject) o, new JSONObject()));
                continue;
            }

            jsonArray.put(o);
        }
        return jsonArray;
    }


    @Override
    public String writeAsString(MappedObject object) {
        return serialize(object, new JSONObject()).toString(2);
    }

    @Override
    @SneakyThrows
    public void writeToFile(MappedObject object, Path file) {
        Files.writeString(file, writeAsString(object), StandardOpenOption.WRITE);
    }

    @Override
    public MappedObject readFromString(String string, Class<?> javaType) {
        return deserialize(new JSONObject(string), javaType);
    }

    @Override
    @SneakyThrows
    public MappedObject readFromFile(Path file, Class<?> javaType) {
        return readFromString(String.join("\n", Files.readAllLines(file)), javaType);
    }
}
