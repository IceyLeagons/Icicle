package net.iceyleagons.icicle.serialization;

import lombok.EqualsAndHashCode;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;

import java.util.Arrays;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public class Test {

    public static void main(String[] args) {
        JsonSerializer json = new JsonSerializer(2);

        Test1 orig = new Test1();
        String serialized = json.convertToString(orig);
        Test1 deser = json.convertFromString(serialized, Test1.class);

        System.out.println(serialized);
        System.out.println();
        System.out.println("Match: " + orig.equals(deser));
        //System.out.println(new JsonSerializer().convertToString(new Test1()));
    }

    @EqualsAndHashCode
    static class Test1 {
        private final String test = "Hello";
        private final String[] test2 = new String[]{"Hello1", "Hello2"};
        private final int test3 = 10;
        private final Test2 test4 = new Test2();
        private final List<Test2> moreTest4 = Arrays.asList(new Test2(), new Test2());
    }

    @EqualsAndHashCode
    static class Test2 {
        private final String test2 = "This is test2";
        private final int[] array = new int[]{1,2,3};
    }

}
