package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.serializers.StringSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.json.JSONObject;

import java.io.File;

public class Test {

    public static void main(String[] args) {
        StringSerializer<JSONObject> stringSerializer = new JsonSerializer(true);

        String output = stringSerializer.serializeToString(new Test2());
        String out2 = stringSerializer.serializeToString(stringSerializer.deserializeFromString(Test2.class, output));

        System.out.println("Matching: " + output.equals(out2));
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
