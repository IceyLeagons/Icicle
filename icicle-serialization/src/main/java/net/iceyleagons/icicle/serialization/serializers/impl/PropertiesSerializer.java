package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.serializers.FileSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesSerializer implements FileSerializer<Properties> {

    @Override
    public Properties serializeObject(Object object) {
        Properties properties = new Properties();
        Map<String, Object> values = new HashMap<>();

        convertToPropertiesFriendlyMap(ObjectMapper.mapObject(object), values, null);
        properties.putAll(values);

        return properties;
    }

    @Override
    @SneakyThrows
    public void writeToFile(Object object, AdvancedFile file) {
        Properties properties = serializeObject(object);

        try (OutputStream outputStream = new FileOutputStream(file.getFile())) {
            properties.store(outputStream, String.format("Serialized form of %s (%s)", object, object.getClass().getName()));
        }
    }

    @Override
    public <T> T deserializeObject(Class<T> wantedType, Properties value) {
        //TODO
        return null;
    }

    @Override
    @SneakyThrows
    public <T> T readFromFile(Class<T> wantedType, AdvancedFile file) {
        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream(file.getFile())) {
            properties.load(inputStream);

            return deserializeObject(wantedType, properties);
        }
    }

    @SuppressWarnings("unchecked")
    private static void convertToPropertiesFriendlyMap(Map<String, Object> object, Map<String, Object> root, String parentName) {
        //This method is necessary because of shitty stuff going inside Properties that causes class cast exceptions, so
        //we cannot pass map inside a map value, and can only have String, because of Integer cannot be casted to String exception

        for (Map.Entry<String, Object> stringObjectEntry : object.entrySet()) {
            String name = stringObjectEntry.getKey();
            String key = parentName == null ? name : String.join(".", parentName, name);
            Object value = stringObjectEntry.getValue();

            if (value instanceof Map) {
                convertToPropertiesFriendlyMap((Map<String, Object>) value, root, key);
                continue;
            }

            root.put(key, value.toString());
        }
    }
}
