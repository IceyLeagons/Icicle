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

import org.bson.Document;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 29, 2022
 */
public class BsonSerializer extends JsonSerializer {

    public BsonSerializer() {
        super(0);
    }

    public <T> T convertFromBson(Document input, Class<T> wantedType) {
        return super.convertFromString(input.toJson(), wantedType); // Very lazy way, could probably optimize this and leave out json
    }

    public Document toBson(Object obj) {
        Document document = new Document();
        document.putAll(super.toJsonObject(obj).toMap()); // Very lazy way, could probably optimize this and leave out json
        return document;
    }


    // Once deserialization is figured out, uncomment this and use it instead of the "lazy method", also testing required for this:

    /*
    private void fromBson(Document value, MappedObject root, Class<?> javaType) {
        for (Field declaredField : javaType.getDeclaredFields()) {
            if (shouldIgnore(declaredField)) continue;
            Class<?> fieldType = declaredField.getType();
            String key = ObjectMapper.getName(declaredField);

            if (ObjectValue.isValuePrimitiveOrString(fieldType)) {
                root.addValue(new ObjectValue(fieldType, declaredField, value.get(key)));
            } else if (ObjectValue.isCollection(fieldType) || ObjectValue.isArray(fieldType)) {
                List<Object> objArray = value.getList(key, Object.class);
                Object[] array = new Object[objArray.size()];

                int i = 0;
                for (Object o : objArray) {
                    if (o instanceof JSONObject) {
                        MappedObject mapped = new MappedObject(fieldType);
                        fromBson((Document) o, mapped, fieldType.arrayType());

                        array[i++] = mapped;
                        continue;
                    }

                    array[i++] = o;
                }

                root.addValue(new ObjectValue(fieldType.arrayType(), declaredField, array));
            } else if (ObjectValue.isMap(fieldType)) {
                //TODO figure out
            } else if (ObjectValue.isSubObject(fieldType)) {
                Document obj = (Document) value.get(key);
                MappedObject mapped = new MappedObject(fieldType);

                fromBson(obj, mapped, fieldType);

                root.addValue(new ObjectValue(fieldType, declaredField, mapped));
            } else {
                throw new IllegalStateException("Unsupported value type: " + fieldType.getName());
            }
        }
    }

    private void toBson(MappedObject mapped, Document root) {
        for (ObjectValue ov : mapped.getValues()) {
            toBson(ov, root);
        }
    }

    private void toBson(ObjectValue value, Document root) {
        if (value.isValuePrimitiveOrString()) {
            root.put(value.getKey(), value.getValue());
        } else if (value.isArray() || value.isCollection()) {
            //We test for collection, because when mapping we convert collections into arrays for easier work later on
            Object array = value.getValue();
            List<Object> newArray = new ArrayList<>();

            for (int i = 0; i < Array.getLength(array); i++) {
                Object o = Array.get(array, i);

                if (o instanceof MappedObject) {
                    Document modoc = new Document();
                    MappedObject mo = (MappedObject) o;

                    toBson(mo, modoc);
                    newArray.add(modoc);
                    continue;
                }

                newArray.add(o);
            }
            root.put(value.getKey(), newArray);
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
                        val = new Document();
                        toBson((MappedObject) entryValue, (Document) val);
                    }


                    obj.put(entryKey.toString(), val);
                }
            }
            root.put(value.getKey(), obj);
        } else if (value.isSubObject()) {
            Document sub = new Document();
            toBson((MappedObject) value.getValue(), sub);
            root.put(value.getKey(), sub);
        } else {
            throw new IllegalStateException("Unsupported value type: " + value.getJavaType().getName());
        }
    }

     */
}
