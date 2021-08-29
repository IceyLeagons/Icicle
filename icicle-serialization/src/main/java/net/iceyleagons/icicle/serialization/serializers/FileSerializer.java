package net.iceyleagons.icicle.serialization.serializers;

import net.iceyleagons.icicle.utilities.file.AdvancedFile;

public interface FileSerializer<V> extends ObjectSerializer<V> {

    void writeToFile(Object object, AdvancedFile file);
    <T> T readFromFile(Class<T> wantedType, AdvancedFile file);

}
