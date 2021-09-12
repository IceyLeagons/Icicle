package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;

public class Test {

    public static void main(String[] args) {
        JsonSerializer jsonSerializer = new JsonSerializer(true);

        String out = jsonSerializer.serializeToString(new Test1());

        Test1 test = jsonSerializer.deSerializeFromString(out, Test1.class);
        String out2 = jsonSerializer.serializeToString(test);

        System.out.println("Matching: " + out.equals(out2));
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
