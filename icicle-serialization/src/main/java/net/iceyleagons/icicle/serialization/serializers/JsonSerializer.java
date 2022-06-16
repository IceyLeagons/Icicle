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

import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.dto.MappedObject;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public class JsonSerializer implements SerializationProvider {

    private JSONObject serialize(MappedObject mappedObject, JSONObject root) {
        for (ObjectValue value : mappedObject.getValues()) {
            if (value.shouldConvert()) {
                Class<?> vClass = value.getValue().getClass();
                root.put(value.getKey(), SerializationUtils.isSubObject(vClass) ? serialize((MappedObject) value.getValue(), new JSONObject()) : value.getValue());
            } else if (value.isValuePrimitiveOrString() || value.isEnum()) {
                root.put(value.getKey(), value.getValue());
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
    public void writeToFile(MappedObject object, Path file) {

    }

    @Override
    public MappedObject readFromString(String string) {
        return null;
    }

    @Override
    public MappedObject readFromFile(Path file) {
        return null;
    }
}
