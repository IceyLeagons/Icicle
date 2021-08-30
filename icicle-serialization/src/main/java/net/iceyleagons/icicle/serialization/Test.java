package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.IcicleApplication;
import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.NBTSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;

import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception {
        Application application = new IcicleApplication("net.iceyleagons.icicle");
        application.start();

        //NBTSerializer serializer = new NBTSerializer(true);
        JsonSerializer jsonSerializer = new JsonSerializer(false);

        Test2 test = new Test2();
        //serializer.writeToFile(test, new AdvancedFile(new File("test.nbt")));


        String output = jsonSerializer.serializeToString(test);

        Test2 newTest2 = jsonSerializer.deserializeFromString(Test2.class, output);
        String out2 = jsonSerializer.serializeToString(newTest2);


        System.out.println("Match: " + output.equals(out2));


        //System.out.println("Serialization process took: " + end + " ms!");
    }

    static class Test2 {
        private final String name = "Test2";
        private final String[] names = new String[] {"asd1", "asd2", "asd3", "asd4"};
        private final int asd = 2;
        private final int[] hello = new int[] {1, 5, 3, 2};

        private final Test3[] test = new Test3[]{new Test3(), new Test3()}; //

        public Test2() {

        }
    }

    static class Test3 {
        private final String name = "Testf";
        private final int hello = 4;
        private final long[] bb = new long[] {5L, 3L, 5L};

        public Test3() {

        }
    }
}
