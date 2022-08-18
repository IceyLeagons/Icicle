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
import net.iceyleagons.nbtlib.NBTConverter;
import net.iceyleagons.nbtlib.streams.NBTInputStream;
import net.iceyleagons.nbtlib.streams.NBTOutputStream;
import net.iceyleagons.nbtlib.tags.Tag;
import net.iceyleagons.nbtlib.tags.TagTypes;
import net.iceyleagons.nbtlib.tags.impl.CompoundTag;
import net.iceyleagons.nbtlib.tags.impl.ListTag;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 20, 2022
 */
public class NbtSerializer implements SerializationProvider {

    private CompoundTag serialize(MappedObject mappedObject, CompoundTag root) {
        root.put("dataVersion", mappedObject.getVersion());
        for (ObjectValue value : mappedObject.getValues()) {
            if (value.shouldConvert()) {
                Class<?> vClass = value.getValue().getClass();
                String key = value.getKey();

                Tag serialized = SerializationUtils.isSubObject(vClass) ? serialize((MappedObject) value.getValue(), new CompoundTag(key)) : NBTConverter.convertPrimitives(vClass, key, value.getValue());
                root.putTag(key, serialized);
            } else if (value.isValuePrimitiveOrString() || value.isEnum()) {
                Class<?> vClass = value.getValue().getClass();
                String key = value.getKey();

                root.putTag(key, NBTConverter.convertPrimitives(vClass, key, value.getValue()));
            } else if (value.isArray() || value.isCollection()) {
                root.putTag(value.getKey(), toList(value.getValue(), value.getKey()));
            } else if (value.isMap()) {
                CompoundTag tag = mapMap(value.getValue(), value.getKey());
                root.putTag(value.getKey(), tag);
            } else if (value.isSubObject()) {
                String key = value.getKey();
                root.putTag(key, serialize((MappedObject) value.getValue(), new CompoundTag(key)));
            }
        }

        return root;
    }

    private CompoundTag mapMap(Object array, String key) {
        CompoundTag tag = new CompoundTag(key);

        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
                Object entryKey = entry.getKey();
                Object entryValue = entry.getValue();

                Tag val = SerializationUtils.isSubObject(entryValue.getClass()) ? serialize((MappedObject) entryValue, new CompoundTag(entryKey.toString())) : NBTConverter.convertPrimitives(entryValue.getClass(), entryKey.toString(), entryValue);
                tag.putTag(entryKey.toString(), val);
            }
        }

        return tag;
    }

    private MappedObject deserialize(CompoundTag tag, Class<?> javaType) {
        final Set<ObjectValue> values = SerializationUtils.getObjectValues(javaType, tag.getInt("dataVersion"))
                .stream()
                .map(o -> deserializeValue(o, tag))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new MappedObject(javaType, values, tag.getInt("dataVersion"));
    }

    private ObjectValue deserializeValue(ObjectValue value, CompoundTag tag) {
        if (value.shouldConvert()) {
            Tag obj = tag.getTag(value.getKey(), Tag.class);
            if (obj instanceof CompoundTag) {
                Object result = deserialize((CompoundTag) obj, value.getJavaType());
                return value.copyWithNewValueAndType(result, value.getJavaType());
            }

            return value.copyWithNewValueAndType(obj.getValue(), value.getJavaType());
        }

        if (value.isValuePrimitiveOrString() || value.isEnum()) {
            return value.copyWithNewValueAndType(tag.getTag(value.getKey(), Tag.class).getValue(), value.getJavaType());
        }

        if (value.isArray()) {
            ListTag listTag = tag.getTag(value.getKey(), ListTag.class);
            return value.copyWithNewValueAndType(
                    fromList(listTag, value.getJavaType().getComponentType()), value.getJavaType()
            );
        }

        if (value.isCollection()) {
            ListTag listTag = tag.getTag(value.getKey(), ListTag.class);
            return value.copyWithNewValueAndType(
                    fromList(listTag, value.getGenericGetter().getGenericClass(0)), value.getJavaType()
            );
        }

        if (value.isMap()) {
            return value.copyWithNewValueAndType(
                    demapMap(tag.getCompound(value.getKey()), value.getGenericGetter().getGenericClass(1)), value.getJavaType()
            );
        }

        if (value.isSubObject()) {
            Tag object = tag.getTag(value.getKey(), Tag.class);
            if (object instanceof CompoundTag) {
                return value.copyWithNewValueAndType(deserialize((CompoundTag) object, value.getJavaType()), value.getJavaType());
            }
        }

        return value.copyWithNewValueAndType(tag.getTag(value.getKey(), Tag.class).getValue(), value.getJavaType());
    }

    private Object demapMap(CompoundTag map, Class<?> valueType) {
        Object array = GenericUtils.createGenericArrayWithoutCasting(Map.Entry.class, map.getValue().size());

        int i = 0;
        for (Map.Entry<String, Tag> entry : map.getValue().entrySet()) {
            Tag entryValue = entry.getValue();
            Object value = SerializationUtils.isSubObject(valueType) ? deserialize((CompoundTag) entryValue, valueType) : entryValue.getValue();

            Array.set(array, i++, Map.entry(entry.getKey(), value));
        }

        return array;
    }

    private Object fromList(ListTag tag, Class<?> javaType) {
        List<Tag> array = tag.getValue();
        Object obj = GenericUtils.createGenericArrayWithoutCasting(javaType, array.size());

        for (int i = 0; i < array.size(); i++) {
            Tag o = array.get(i);

            if (o instanceof CompoundTag) {
                Array.set(obj, i, deserialize((CompoundTag) o, javaType));
                continue;
            }

            Array.set(obj, i, o.getValue());
        }

        return obj;
    }

    private Tag toList(Object array, String key) {
        List<Object> objects = new ArrayList<>();
        boolean subFlag = false;

        for (int i = 0; i < Array.getLength(array); i++) {
            Object o = Array.get(array, i);

            if (o instanceof MappedObject) {
                subFlag = true;
                objects.add(serialize((MappedObject) o, new CompoundTag(String.valueOf(i))));
                continue;
            }
            objects.add(o);
        }

        return getListTag(key, subFlag, objects);
    }

    private ListTag getListTag(String key, boolean isSubObject, List<Object> values) {
        if (isSubObject) {
            return new ListTag(key, TagTypes.COMPOUND, values.stream().map(o -> (CompoundTag) o).collect(Collectors.toList()));
        }

        if (values.size() == 0) {
            return new ListTag(key, TagTypes.COMPOUND, Collections.emptyList());
        }

        return NBTConverter.getList(values.get(0).getClass(), key, values);
    }

    @Override
    @SneakyThrows
    public void writeToFile(MappedObject object, Path file) {
        CompoundTag compoundTag = serialize(object, new CompoundTag("root"));

        try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.WRITE)) {
            try (NBTOutputStream outputStream = new NBTOutputStream(os)) {
                outputStream.writeTag(compoundTag);
            }
        }
    }

    @Override
    @SneakyThrows
    public MappedObject readFromFile(Path file, Class<?> javaType) {
        try (InputStream is = Files.newInputStream(file, StandardOpenOption.READ)) {
            try (NBTInputStream inputStream = new NBTInputStream(is)) {
                Tag tag = inputStream.readTag();
                return deserialize((CompoundTag) tag, javaType);
            }
        }
    }

    @Override
    public String writeAsString(MappedObject object) {
        throw new IllegalStateException("String serialization is unsupported by the NBT format!");
    }

    @Override
    public MappedObject readFromString(String string, Class<?> javaType) {
        throw new IllegalStateException("String deserialization is unsupported by the NBT format!");
    }
}
