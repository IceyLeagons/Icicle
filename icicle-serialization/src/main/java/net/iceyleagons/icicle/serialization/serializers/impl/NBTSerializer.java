package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.serializers.FileSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class NBTSerializer implements FileSerializer {

    private boolean compress;

    public NBTSerializer(boolean compress) {
        this.compress = compress;
    }

    public NBTSerializer() {
        this(true);
    }

    @Override
    @SneakyThrows
    public void writeToFile(Object object, AdvancedFile file) {
        CompoundTag compoundTag = (CompoundTag) this.serializeObject(object);
        NBTUtil.write(compoundTag, file.getFile(), compress);
    }

    @Override
    public Object readFromFile(Class wantedType, AdvancedFile file) {
        return null;
    }

    @Override
    public Object serializeObject(Object object) {
        CompoundTag compoundTag = new CompoundTag();
        Map<String, Object> values = ObjectMapper.mapObject(object);

        for (Map.Entry<String, Object> stringObjectEntry : values.entrySet()) {
            addToTag(compoundTag, stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }

        return compoundTag;
    }

    @Override
    public Object deserializeObject(Class wantedType, Object value) {
        return null;
    }

    private static void addToTag(CompoundTag compoundTag, String name, Object object) {
        if (object instanceof Boolean) {
            compoundTag.putBoolean(name, (Boolean) object);
        } else if (object instanceof Byte) {
            compoundTag.putByte(name, (Byte) object);
        } else if (object instanceof byte[]) {
            compoundTag.putByteArray(name, (byte[]) object);
        } else if (object instanceof Double) {
            compoundTag.putDouble(name, (Double) object);
        } else if (object instanceof Float) {
            compoundTag.putFloat(name, (Float) object);
        } else if (object instanceof Integer) {
            compoundTag.putInt(name, (Integer) object);
        } else if (object instanceof int[]) {
            compoundTag.putIntArray(name, (int[]) object);
        } else if (object instanceof Long) {
            compoundTag.putLong(name, (Long) object);
        } else if (object instanceof long[]) {
            compoundTag.putLongArray(name, (long[]) object);
        } else if (object instanceof Short) {
            compoundTag.putShort(name, (Short) object);
        } else if (object instanceof Map) {
            Map<?,?> map = (Map<?,?>) object;
            CompoundTag mapTag = new CompoundTag();

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                addToTag(mapTag, String.valueOf(entry.getKey()), entry.getValue());
            }

            compoundTag.put(name, mapTag);
        } else if (object instanceof Collection) {
            Object[] list = ((Collection<?>) object).toArray();
            CompoundTag listTag = new CompoundTag();

            for (int i = 0; i < list.length; i++) {
                addToTag(listTag, String.valueOf(i), list[i]);
            }

            compoundTag.put(name, listTag);
        } else if (object instanceof String[]) {
            ListTag<StringTag> list = new ListTag<>(StringTag.class);

            String[] array = (String[]) object;
            for (String s : array) {
                list.add(new StringTag(s));
            }

            compoundTag.put(name, list);
        } else {
            compoundTag.putString(name, object.toString());
        }
    }
}
