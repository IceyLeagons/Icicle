package net.iceyleagons.icicle.serialization;

import lombok.EqualsAndHashCode;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class Test {

    public static void main(String[] args) {
        AbstractSerializer serializer = new JsonSerializer(true);

        Test1 test = new Test1();
        long start = System.currentTimeMillis();
        String serialized = serializer.serializeToString(test);
        long end = System.currentTimeMillis() - start;


        Test1 deserialized = serializer.deSerializeFromString(serialized, Test1.class);
        String t = serializer.serializeToString(deserialized);


        System.out.println(serialized);
        System.out.println(t);
        System.out.println("Test: " + t.equals(serialized));
        System.out.println("Serialization took: " + end + "ms");
    }

    @EqualsAndHashCode
    static class Test1 {

        private final UUID uuid = UUID.randomUUID();
        private final String name = "Test1";
        private final int id = 20;
        private final List<String> test = Arrays.asList("hello", "hello2");
        private final String[] alias = new String[]{"t1", "tst1"};

        private final Test2 test2 = new Test2();
        private final Test2[] tests = new Test2[]{new Test2(), new Test2()};

        public Test1() {
        }

    }

    static class Test2 {
        private final String name = "Test2";
        private final int id = 30;
        private final boolean asd = true;

        public Test2() {
        }
    }
}
