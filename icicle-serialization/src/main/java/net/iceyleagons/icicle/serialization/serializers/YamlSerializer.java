package net.iceyleagons.icicle.serialization.serializers;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.AbstractSerializer;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.simpleyaml.configuration.file.YamlFile;

import java.lang.reflect.Field;
import java.util.List;

public class YamlSerializer extends AbstractSerializer {

    @Override
    @SneakyThrows
    protected void serializeToFile(ObjectDescriptor objectDescriptor, AdvancedFile file) {
        YamlFile yamlFile = new YamlFile(file.getFile());
        yamlFile.createNewFile(true);

        addFromDescriptor(objectDescriptor, yamlFile, null);
        yamlFile.save();
    }

    @Override
    protected ObjectDescriptor getFromFile(AdvancedFile file, Class<?> type) {
        // TODO
        return super.getFromFile(file, type);
    }

    private static void addFromDescriptor(ObjectDescriptor objectDescriptor, YamlFile yamlFile, String root) {
        // Handling default Java types (String, int, long, etc.)
        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            String key = valueField.getA();
            Object value = valueField.getC();

            yamlFile.set(root == null ? key : root + "." + key, value);
        }

        // Handling sub-object non-array types
        for (Triple<String, Field, ObjectDescriptor> subObject : objectDescriptor.getSubObjects()) {
            String key = subObject.getA();
            ObjectDescriptor subDescriptor = subObject.getC();

            addFromDescriptor(subDescriptor, yamlFile, key);
        }

        // Handling sub-object array types
        for (Triple<String, Field, List<ObjectDescriptor>> subObjectArray : objectDescriptor.getSubObjectArrays()) {
            String key = subObjectArray.getA();

            int count = 0; //maybe this count is "janky", but could not come up with anything else
            for (ObjectDescriptor descriptor : subObjectArray.getC()) {
                addFromDescriptor(descriptor, yamlFile, key + count++);
            }
        }
    }

}
