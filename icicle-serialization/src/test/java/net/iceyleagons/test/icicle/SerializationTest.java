/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.test.icicle;

import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.annotations.EnumSerialization;
import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.impl.*;
import net.iceyleagons.icicle.serialization.serializers.NbtSerializer;
import net.iceyleagons.icicle.utilities.Benchmark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public class SerializationTest {

    @BeforeAll
    public static void registerMappers() {
        // Normally this would be registered via Icicle-Core, but I didn't want to bootstrap Icicle for serialization test so yeah
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new ArrayMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new CollectionMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new EnumMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new MapMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new UUIDMapper());
        ConverterAnnotationHandler.REGISTERED_CONVERTERS.add(new Test3Converter());
    }

    @org.junit.jupiter.api.Test
    public void testNbt() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper(new NbtSerializer());
        Test original = new Test("asd", 4, UUID.randomUUID(),
                new String[]{"value1", "value2", "value3"}, new int[]{35, 65, 45},
                List.of("asd", "asd3"), Map.of("testkey", "testvalue", "key2", "value2"),
                new Test2(), new Test3());

        File f = new File("test.nbt");
        if (!f.exists()) {
            f.createNewFile();
        }
        objectMapper.writeValueToFile(original, f.toPath());

        Test clone = objectMapper.readValueFromFile(f.toPath(), Test.class);
        compare(original, clone);
    }

    @org.junit.jupiter.api.Test
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();

        Test original = new Test("asd", 4, UUID.randomUUID(),
                new String[]{"value1", "value2", "value3"}, new int[]{35, 65, 45},
                List.of("asd", "asd3"), Map.of("testkey", "testvalue", "key2", "value2"),
                new Test2(), new Test3());

        String ser1 = Benchmark.run(() -> objectMapper.writeValueAsString(original), "Serialization");
        System.out.println(ser1);
        Test test = Benchmark.run(() -> objectMapper.readValueFromString(ser1, Test.class), "DeSerialization");


        compare(original, test);
    }

    public void compare(String key, Object a, Object b) {
        boolean value = a.equals(b);
        System.out.printf("[%s] %s: %s <--> %s\n",value ? "CHECK" : "ERROR", key, a, b);
        Assertions.assertTrue(value);
    }

    public boolean compareMaps(Map<?,?> a, Map<?,?> b) {
        System.out.println(Arrays.toString(a.entrySet().toArray(Map.Entry<?, ?>[]::new)));
        System.out.println(Arrays.toString(b.entrySet().toArray(Map.Entry<?, ?>[]::new)));

        for (Map.Entry<?, ?> entry : a.entrySet()) {
            if (!b.containsKey(entry.getKey()) || !entry.getValue().equals(b.get(entry.getKey()))) {
                Assertions.fail();
            }
        }
        return true;
    }

    public void compare(Test a, Test b) {
        compare("Name", a.name, b.name);
        compare("Number", a.number, b.number);
        compare("UUID", a.uuid.toString(), b.uuid.toString());
        compare("List", Arrays.toString(a.list), Arrays.toString(b.list));
        compare("NumberList", Arrays.toString(a.numberList), Arrays.toString(b.numberList));
        compare("List", Arrays.toString(a.stringList.toArray()), Arrays.toString(b.stringList.toArray()));
        compare("subObject - name", a.subObject.name, b.subObject.name);
        compare("subObject - list", Arrays.toString(a.subObject.list), Arrays.toString(a.subObject.list));
        compare("subObject - testEnum", a.subObject.testEnum.name(), b.subObject.testEnum.name());
        compare("test3 - name", a.test3.name, b.test3.name);

        System.out.printf("[%s] Comparing mapTest", compareMaps(a.mapTest, b.mapTest) ? "CHECK" : "ERROR");
    }

    static class Test {
        public String name;

        public int number;
        public UUID uuid;

        public String[] list;
        public int[] numberList;

        public List<String> stringList;
        public Map<String, String> mapTest;

        public Test2 subObject;

        @Convert(converter = Test3Converter.class)
        public Test3 test3;

        public Test(String name, int number, UUID uuid, String[] list, int[] numberList, List<String> stringList, Map<String, String> mapTest, Test2 subObject, Test3 test3) {
            this.name = name;
            this.number = number;
            this.uuid = uuid;
            this.list = list;
            this.numberList = numberList;
            this.stringList = stringList;
            this.mapTest = mapTest;
            this.subObject = subObject;
            this.test3 = test3;
        }

        public Test() {

        }
    }

    static class Test2 {
        public String name = "Hello2";
        public String[] list = new String[]{"ranomd", "random2"};

        @EnumSerialization(EnumMapper.EnumMappingType.NAME)
        public TestEnum testEnum = TestEnum.HELLO2;
    }

    static class Test3 {

        public String name;

        public Test3(String name) {
            this.name = name;
        }

        public Test3() {
            this("Hi");
        }
    }

    static enum TestEnum {
        HELLO, HELLO2, HELLO3;
    }

    static class Test3Converter extends ValueConverter<Test3, String> {

        @Override
        protected String convertToSerializedValue(Test3 input) {
            return input.name;
        }

        @Override
        protected Test3 convertToObjectValue(String serialized) {
            return new Test3(serialized);
        }
    }
}
