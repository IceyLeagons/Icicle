package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.File;

public abstract class AbstractSerializer {

    private final ObjectMapper objectMapper;

    public AbstractSerializer() {
        this.objectMapper = new ObjectMapper();
    }

    // ============ [ String Serialization/DeSerialization ] ============ //

    public String serializeToString(Object object) {
        return this.serializeToString(this.objectMapper.mapObject(object));
    }

    public <T> T deSerializeFromString(String string, Class<T> type) {
        ObjectDescriptor objectDescriptor = this.getFromString(string, type);

        return this.objectMapper.unMapObject(objectDescriptor, type);
    }

    @Internal
    protected String serializeToString(ObjectDescriptor objectDescriptor) {
        // Method is here as a default, implementation of such serialization should happen with overriding.

        throw new IllegalStateException("This serializer does not support serialization to string.");
    }

    @Internal
    protected ObjectDescriptor getFromString(String string, Class<?> type) {
        // Method is here as a default, implementation of such deserialization should happen with overriding.

        throw new IllegalStateException("This serializer does not support deserialization from string.");
    }

    // ============ [ File Serialization/DeSerialization ] ============ //

    public void serializeToFile(Object object, File file) {
        this.serializeToFile(this.objectMapper.mapObject(object), new AdvancedFile(file));
    }

    public void serializeToFile(Object object, AdvancedFile file) {
        this.serializeToFile(this.objectMapper.mapObject(object), file);
    }

    public <T> T deserializeFromFile(File file, Class<T> type) {
        return this.deserializeFromFile(new AdvancedFile(file), type);
    }

    public <T> T deserializeFromFile(AdvancedFile file, Class<T> type) {
        ObjectDescriptor objectDescriptor = this.getFromFile(file, type);

        return this.objectMapper.unMapObject(objectDescriptor, type);
    }

    @Internal
    protected void serializeToFile(ObjectDescriptor objectDescriptor, AdvancedFile file) {
        // Method is here as a default, implementation of such serialization should happen with overriding.

        throw new IllegalStateException("This serializer does not support serialization to file.");
    }

    @Internal
    protected ObjectDescriptor getFromFile(AdvancedFile file, Class<?> type) {
        // Method is here as a default, implementation of such deserialization should happen with overriding.

        throw new IllegalStateException("This serializer does not support deserialization from file.");
    }
}
