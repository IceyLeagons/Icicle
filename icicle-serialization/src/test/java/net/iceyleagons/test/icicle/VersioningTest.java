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
import net.iceyleagons.icicle.serialization.annotations.versioning.Since;
import net.iceyleagons.icicle.serialization.annotations.versioning.Version;
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.mapping.impl.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 18, 2022
 */
public class VersioningTest {

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
        String oldVersion = "{\"dataVersion\": 0, \"hello\": \"asd\"}}";
        String newVersion = "{\"thisWillBeNull\": \"FilledIn\", \"dataVersion\": 1, \"hello\": \"asd\"}}";

        Test1 old = objectMapper.readValueFromString(oldVersion, Test1.class);
        Test1 newV = objectMapper.readValueFromString(newVersion, Test1.class);

        Assertions.assertEquals("asd", old.hello);
        Assertions.assertEquals("asd", newV.hello);

        Assertions.assertNull(old.thisWillBeNull);
        Assertions.assertEquals("FilledIn", newV.thisWillBeNull);
    }

    @Version(1)
    static class Test1 {
        @Since(0)
        public String hello;
        @Since(1)
        public String thisWillBeNull;

        public Test1() {

        }

        public Test1(String hello, String hello2) {
            this.hello = hello;
            this.thisWillBeNull = hello2;
        }

        @Override
        public boolean equals(Object obj) {
            Test1 test1 = (Test1) obj;
            return test1.hello.equals(this.hello) && Objects.equals(this.thisWillBeNull, test1.thisWillBeNull);
        }
    }
}
