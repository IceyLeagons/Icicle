package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.IcicleApplication;
import net.iceyleagons.icicle.serialization.serializers.impl.NBTSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception {
        Application application = new IcicleApplication("net.iceyleagons.icicle");
        application.start();

        NBTSerializer serializer = new NBTSerializer(true);

        serializer.writeToFile(new Test2(), new AdvancedFile(new File("test.nbt")));
    }

    static class Test2 {
        private final String name = "Test2";
    }
}
