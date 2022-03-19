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

package net.iceyleagons.test.icicle.serialization;

import net.iceyleagons.icicle.serialization.annotations.Convert;
import net.iceyleagons.icicle.serialization.converters.builtin.UUIDConverter;
import net.iceyleagons.icicle.serialization.serializers.impl.BsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.YamlSerializer;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 14, 2022
 */
public class SerializationTest {

    @Test
    @DisplayName("JSON (String) - Serialization & Deserialization")
    public void testJson() {
        JsonSerializer serializer = new JsonSerializer();
        TestClass original = new TestClass();

        String serialized = Assertions.assertDoesNotThrow(() -> serializer.serializeToString(original));
        TestClass clone = Assertions.assertDoesNotThrow(() -> serializer.deserializeFromString(serialized, TestClass.class));

        Assertions.assertEquals(original.uuid.toString(), clone.uuid.toString());
        Assertions.assertEquals(original.name, clone.name);
        Assertions.assertArrayEquals(original.list, clone.list);
        Assertions.assertEquals(original.number, clone.number);
        Assertions.assertArrayEquals(original.numberList, clone.numberList);
        Assertions.assertIterableEquals(original.stringList, clone.stringList);

        Assertions.assertNotNull(clone.subObject);
        Assertions.assertEquals(original.subObject.name, clone.subObject.name);
        Assertions.assertIterableEquals(original.mapTest.entrySet(), clone.mapTest.entrySet());
    }

    @Test
    @DisplayName("BSON (Document) - Serialization & Deserialization")
    public void testBson() {
        BsonSerializer serializer = new BsonSerializer();
        TestClass original = new TestClass();

        Document serialized = Assertions.assertDoesNotThrow(() -> serializer.serialize(original));
        TestClass clone = Assertions.assertDoesNotThrow(() -> serializer.deserialize(serialized, TestClass.class));

        Assertions.assertEquals(original.uuid.toString(), clone.uuid.toString());
        Assertions.assertEquals(original.name, clone.name);
        Assertions.assertArrayEquals(original.list, clone.list);
        Assertions.assertEquals(original.number, clone.number);
        Assertions.assertArrayEquals(original.numberList, clone.numberList);
        Assertions.assertIterableEquals(original.stringList, clone.stringList);

        Assertions.assertNotNull(clone.subObject);
        Assertions.assertEquals(original.subObject.name, clone.subObject.name);
        Assertions.assertIterableEquals(original.mapTest.entrySet(), clone.mapTest.entrySet());
    }

    @Test
    @DisplayName("YAML (File) - Serialization & Deserialization")
    public void testYaml() {
        YamlSerializer serializer = new YamlSerializer();
        TestClass original = new TestClass();
        Path file = new File("test.yml").toPath();

        Assertions.assertDoesNotThrow(() -> serializer.serializeToPath(original, file));
        TestClass clone = Assertions.assertDoesNotThrow(() -> serializer.deserializeFromPath(file, TestClass.class));

        Assertions.assertEquals(original.uuid.toString(), clone.uuid.toString());
        Assertions.assertEquals(original.name, clone.name);
        Assertions.assertArrayEquals(original.list, clone.list);
        Assertions.assertEquals(original.number, clone.number);
        Assertions.assertArrayEquals(original.numberList, clone.numberList);
        Assertions.assertIterableEquals(original.stringList, clone.stringList);

        Assertions.assertNotNull(clone.subObject);
        Assertions.assertEquals(original.subObject.name, clone.subObject.name);
        Assertions.assertIterableEquals(original.mapTest.entrySet(), clone.mapTest.entrySet());

        try {
            Files.delete(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class TestClass {
        @Convert(UUIDConverter.class)
        public UUID uuid = UUID.randomUUID();
        public String name = "Hello";
        public String[] list = new String[]{"asd", "asd2"};
        public int number = 4;
        public int[] numberList = new int[]{1, 2, 3};
        public List<String> stringList = Arrays.asList("test1", "test2", "test3");
        public TestSubClass subObject = new TestSubClass();
        public Map<String, String> mapTest = Map.of("testkey", "testvalue", "key2", "value2");
    }

    static class TestSubClass {
        public String name = "Test2 field";
    }
}
