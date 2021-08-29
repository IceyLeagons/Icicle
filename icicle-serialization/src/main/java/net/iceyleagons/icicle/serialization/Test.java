package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.PropertiesSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.XmlSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        XmlSerializer xmlSerializer = new XmlSerializer();

        xmlSerializer.writeToFile(new Test2(), new AdvancedFile(new File("hello.xml")));
    }

    static class Test2 {
        private final String name = "Test2";
        //private final int id = 1;

        private final Test3 object = new Test3();
    }

    static class Test3 {
        private final String name = "Test3";
        //private final int id = 2;

        private final Test4 anotherChild = new Test4();
    }

    static class Test4 {
        private final String name = "Test4";
        private final int id = 3;
        private boolean last = true;
    }

}
