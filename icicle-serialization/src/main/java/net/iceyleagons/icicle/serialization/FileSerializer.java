package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.utilities.file.AdvancedFile;

public interface FileSerializer {

    void writeToFile(AdvancedFile file);
    void readFromFile(AdvancedFile file);

}
