package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.serializers.YamlSerializer;
import net.iceyleagons.icicle.serialization.serializers.nbt.NBTSerializer;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class Test {

    public static void main(String[] args) {
        AbstractSerializer serializer = new YamlSerializer();

        serializer.serializeToFile(new Test1(), new File("test.yml"));
    }

    static class Test1 {

        private final String name = "Test1";
        private final int id = 20;
        private final String[] alias = new String[] { "t1", "tst1"};

        private final Test2 test2 = new Test2();
        private final Test2[] tests = new Test2[]{new Test2(), new Test2()};

        public Test1() {}

    }

    static class Test2 {
        private final String name = "Test2";
        private final int id = 30;
        private final boolean asd = true;

        public Test2() {}
    }
}
