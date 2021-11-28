package net.iceyleagons.icicle.serialization.serializers;

import java.io.File;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public interface FileSerializer {

    void serializeToFile(File file, Object object);

    <T> T deSerializeFromFile(File file, Class<T> wantedType);

}
