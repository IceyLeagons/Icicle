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
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.impl.*;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;
import net.iceyleagons.icicle.utilities.Benchmark;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public class TestMain {

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
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        String asd = Benchmark.run(() -> objectMapper.writeValueAsString(new Test()), "Serialization");

        System.out.println(asd);
    }

    static class Test {
        public String name = "Hello";

        public int number = 4;
        public UUID uuid = UUID.randomUUID();

        public String[] list = new String[]{"asd", "asd2"};
        public int[] numberList = new int[]{1, 2, 3};

        public List<String> stringList = Arrays.asList("test1", "test2", "test3");
        public Map<String, String> mapTest = Map.of("testkey", "testvalue", "key2", "value2");

        public Test2 subObject = new Test2();

        @Convert(converter = Test3Converter.class)
        public Test3 test3 = new Test3();
    }

    static class Test2 {
        public String name = "Hello2";
        public String[] list = new String[]{"ranomd", "random2"};

        @EnumSerialization(EnumMapper.EnumMappingType.ORDINAL)
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
