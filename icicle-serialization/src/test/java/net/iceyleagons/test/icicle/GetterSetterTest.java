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
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.impl.*;
import net.iceyleagons.icicle.utilities.Benchmark;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 18, 2022
 */
public class GetterSetterTest {

    @BeforeAll
    public static void registerMappers() {
        // Normally this would be registered via Icicle-Core, but I didn't want to bootstrap Icicle for serialization test so yeah
        if (!PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.isEmpty()) return;

        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new ArrayMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new CollectionMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new EnumMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new MapMapper());
        PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS.add(new UUIDMapper());
        ConverterAnnotationHandler.REGISTERED_CONVERTERS.add(new SerializationTest.Test3Converter());
    }

    @Test
    public void test() {
        ObjectMapper objectMapper = new ObjectMapper();
        Test1 orig = new Test1();
        orig.hello = "asd";

        String s = Benchmark.run(() -> objectMapper.writeValueAsString(orig), "Serialization");
        Test1 read = Benchmark.run(() -> objectMapper.readValueFromString(s, Test1.class), "DeSerialization");

        Assertions.assertEquals(orig, read);
    }

    static class Test1 {
        public String hello;

        public Test1() {

        }

        public Test1(String hello) {
            this.hello = hello;
        }
        
        public String getHello() {
            System.out.println("Invoked getter");
            return this.hello;
        }

        public void setHello(String value) {
            System.out.println("Invoked setter");
            this.hello = value;
        }

        @Override
        public boolean equals(Object obj) {
            return hello.equals(((Test1) obj).hello);
        }
    }
}
