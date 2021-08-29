package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.*;
import java.util.Properties;

/**
 * Using the laziest solution lmao (may move to proper implementation):
 *
 * Extending {@link PropertiesSerializer} and Overriding the
 * {@link net.iceyleagons.icicle.serialization.serializers.FileSerializer#readFromFile(Class, AdvancedFile)} and
 * {@link net.iceyleagons.icicle.serialization.serializers.FileSerializer#writeToFile(Object, AdvancedFile)} to use storeToXml and loadFromXml
 */
public class XmlSerializer extends PropertiesSerializer {

    @Override
    @SneakyThrows
    public void writeToFile(Object object, AdvancedFile file) {
        Properties properties = serializeObject(object);

        try (OutputStream outputStream = new FileOutputStream(file.getFile())) {
            properties.storeToXML(outputStream, String.format("Serialized form of %s (%s)", object, object.getClass().getName()));
        }
    }

    @Override
    @SneakyThrows
    public <T> T readFromFile(Class<T> wantedType, AdvancedFile file) {
        Properties properties = new Properties();

        try (InputStream inputStream = new FileInputStream(file.getFile())) {
            properties.loadFromXML(inputStream);

            return deserializeObject(wantedType, properties);
        }
    }
}
