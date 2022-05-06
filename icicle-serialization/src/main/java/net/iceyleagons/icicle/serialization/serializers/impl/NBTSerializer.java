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
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import net.iceyleagons.nbtlib.NBTConverter;
import net.iceyleagons.nbtlib.Tag;
import net.iceyleagons.nbtlib.TagTypes;
import net.iceyleagons.nbtlib.streams.NBTInputStream;
import net.iceyleagons.nbtlib.streams.NBTOutputStream;
import net.iceyleagons.nbtlib.tags.CompoundTag;
import net.iceyleagons.nbtlib.tags.ListTag;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.iceyleagons.nbtlib.NBTConverter.getAppropriateTagClass;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 24, 2022
 */
public class NBTSerializer implements FileSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CompoundTag serialize(Object object) {
        return serialize(object, new CompoundTag("main"));
    }

    private CompoundTag serialize(Object object, CompoundTag root) {
        SerializedObject obj = objectMapper.mapObject(object);
        return serialize(obj, root);
    }

    private CompoundTag serialize(SerializedObject serializedObject, CompoundTag root) {
        for (ObjectValue value : serializedObject.getValues()) {
            if (value.shouldConvert()) {
                Class<?> vClass = value.getValue().getClass();
                root.putTag(value.getKey(), getValue(value.getKey(), vClass, value.getValue()));
            } else if (value.isValuePrimitiveOrString() || value.isEnum()) {
                root.putTag(value.getKey(), toTag(value.getKey(), value.getValue()));
            } else if (value.isArray()) {
                root.putTag(value.getKey(), toListTag(value.getKey(), value.getValue(), value.getField().getType().getComponentType()));
            } else if (value.isCollection()) {
                root.putTag(value.getKey(), toListTag(value.getKey(), value.getValue(), GenericUtils.getGenericTypeClass(value.getField(), 0)));
            } else if (value.isMap()) {
                root.putTag(value.getKey(), mapMap(value.getKey(), value.getValue()));
            } else if (value.isSubObject()) {
                root.putTag(value.getKey(), serialize((SerializedObject) value.getValue(), new CompoundTag(value.getKey())));
            }
        }

        return root;
    }

    public <T> T deserializeObject(CompoundTag tag, Class<T> type) {
        return objectMapper.demapObject(deserialize(tag, type), type);
    }

    private SerializedObject deserialize(CompoundTag tag, Class<?> javaType) {
        Set<ObjectValue> values = SerializationUtils.getValuesForClass(javaType, (field, key) -> {
            if (SerializationUtils.shouldConvert(field)) {
                Tag t = tag.getTag(key, Tag.class);
                if (t instanceof CompoundTag) {
                    return deserialize((CompoundTag) t, field.getType());
                }

                return t.getValue();
            } else if (SerializationUtils.isValuePrimitiveOrString(field.getType()) || SerializationUtils.isEnum(field.getType())) {
                return tag.getTag(key, Tag.class).getValue();
            } else if (SerializationUtils.isArray(field.getType())) {
                return fromListTag(tag.getTag(key, ListTag.class), field.getType().getComponentType());
            } else if (SerializationUtils.isCollection(field.getType())) {
                return fromListTag(tag.getTag(key, ListTag.class), GenericUtils.getGenericTypeClass(field, 0));
            } else if (SerializationUtils.isMap(field.getType())) {
                return demapMap(tag.getTag(key, CompoundTag.class), GenericUtils.getGenericTypeClass(field, 1));
            } else if (SerializationUtils.isSubObject(field.getType())) {
                return deserialize(tag.getTag(key, CompoundTag.class), field.getType());
            }

            return null;
        });

        SerializedObject obj = new SerializedObject(javaType);
        obj.getValues().addAll(values);

        return obj;
    }

    private CompoundTag mapMap(String key, Object array) {
        CompoundTag tag = new CompoundTag(key);

        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                String entryKey = entry.getKey().toString();
                Object entryValue = entry.getValue();

                tag.putTag(entryKey, getValue(entryKey, entryValue.getClass(), entryValue));
            }
        }

        return tag;
    }

    private Object demapMap(CompoundTag object, Class<?> valueType) {
        final Map<String, Tag> tags = object.getValue();
        Object array = GenericUtils.createGenericArrayWithoutCasting(Map.Entry.class, tags.size());

        int i = 0;
        for (Map.Entry<String, Tag> entry : tags.entrySet()) {
            Tag entryValue = entry.getValue();
            Object value = SerializationUtils.isSubObject(valueType) ? deserialize((CompoundTag) entryValue, valueType) : entryValue.getValue();
            Array.set(array, i++, Map.entry(entry.getKey(), value));
        }

        return array;
    }

    private Object fromListTag(ListTag listTag, Class<?> compType) {
        final List<Tag> array = listTag.getValue();
        Object obj = GenericUtils.createGenericArrayWithoutCasting(compType, array.size());

        for (int i = 0; i < array.size(); i++) {
            Object o = array.get(i).getValue();

            if (o instanceof CompoundTag) {
                Array.set(obj, i, deserialize((CompoundTag) o, compType));
                continue;
            }

            Array.set(obj, i, o);
        }

        return obj;
    }

    private ListTag toListTag(String key, Object array, Class<?> compType) {
        List<Tag> tags = new ArrayList<>();
        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);

            if (o instanceof SerializedObject) {
                tags.add(serialize((SerializedObject) o, new CompoundTag(key)));
                continue;
            }

            tags.add(NBTConverter.convertPrimitive(key, o));
        }

        TagTypes type = TagTypes.getByClass(getAppropriateTagClass(compType));
        return new ListTag(key, type, tags);
    }

    @Override
    @SneakyThrows
    public void serializeToPath(Object object, Path path) {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }

        try (OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.WRITE)) {
            try (NBTOutputStream nbtOutputStream = new NBTOutputStream(outputStream)) {
                nbtOutputStream.writeTag(serialize(object));
            }
        }
    }

    @Override
    @SneakyThrows
    public <T> T deserializeFromPath(Path path, Class<T> type) {
        try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ)) {
            try (NBTInputStream nbtInputStream = new NBTInputStream(inputStream)) {
                return deserializeObject((CompoundTag) nbtInputStream.readTag(), type);
            }
        }
    }

    private Tag getValue(String name, Class<?> clazz, Object value) {
        return SerializationUtils.isSubObject(clazz) ? serialize((SerializedObject) value, new CompoundTag(name)) : toTag(name, value);
    }

    private static Tag toTag(String name, Object obj) {
        return NBTConverter.convertPrimitive(name, obj);
    }
}
