package net.iceyleagons.icicle.serialization.serializers.nbt;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.AbstractSerializer;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.querz.nbt.io.NBTUtil;
import net.querz.nbt.tag.CompoundTag;
import net.querz.nbt.tag.Tag;

import java.lang.reflect.Field;
import java.util.List;

public class NBTSerializer extends AbstractSerializer {

    @Override
    @SneakyThrows
    protected void serializeToFile(ObjectDescriptor objectDescriptor, AdvancedFile file) {
        CompoundTag root = buildFromDescriptor(objectDescriptor);
        NBTUtil.write(root, file.getFile());
    }

    @Override
    protected ObjectDescriptor getFromFile(AdvancedFile file, Class<?> type) {
        // TODO when lists etc. are figured out
        return super.getFromFile(file, type);
    }

    private CompoundTag buildFromDescriptor(ObjectDescriptor objectDescriptor) {
        CompoundTag root = new CompoundTag();

        // Handling default Java types (String, int, long, etc.)
        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            String key = valueField.getA();
            Object value = valueField.getC();

            try {
                Tag tag = NBTUtils.getAsTag(value);
                root.put(key, tag);
            } catch (IllegalArgumentException e) {
                e.printStackTrace(); // TODO logger
            }
        }

        // Handling sub-object non-array types
        for (Triple<String, Field, ObjectDescriptor> subObject : objectDescriptor.getSubObjects()) {
            String key = subObject.getA();
            ObjectDescriptor subDescriptor = subObject.getC();

            root.put(key, buildFromDescriptor(subDescriptor));
        }

        // Handling sub-object array types
        for (Triple<String, Field, List<ObjectDescriptor>> subObjectArray : objectDescriptor.getSubObjectArrays()) {
            // TODO when I have any idea how to do it the best way
        }

        return root;
    }
}
