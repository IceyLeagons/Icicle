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

package net.iceyleagons.icicle.serialization;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.NBTSerializer;
import net.iceyleagons.icicle.utilities.Benchmark;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 26, 2022
 */
public class Test {

    @SneakyThrows
    public static void main(String[] args) {
        NBTSerializer nbt = new NBTSerializer();
        Test1 original = new Test1();

        nbt.serializeToPath(original, new File("test.nbt").toPath());
        Test1 serialized = nbt.deserializeFromPath(new File("test.nbt").toPath(), Test1.class);

        System.out.println(original.name + " <--> " + serialized.name);
        System.out.println(Arrays.toString(original.list) + " <--> " + Arrays.toString(serialized.list));
        System.out.println(original.number + " <--> " + serialized.number);
        System.out.println(Arrays.toString(original.numberList) + " <--> " + Arrays.toString(serialized.numberList));
        System.out.println(Arrays.toString(original.stringList.toArray(String[]::new)) + " <--> " + Arrays.toString(original.stringList.toArray(String[]::new)));
        System.out.println(original.subObject.name + " <--> " + serialized.subObject.name);
        System.out.println(Arrays.toString(original.mapTest.entrySet().toArray(Map.Entry[]::new)) + " <--> " + Arrays.toString(original.mapTest.entrySet().toArray(Map.Entry[]::new)));


        System.out.println("Equals: " + original.equals(serialized));


    }

    @EqualsAndHashCode
    static class Test1 {
        public String name = "Hello";
        public String[] list = new String[]{"asd", "asd2"};
        public int number = 4;
        public int[] numberList = new int[]{1, 2, 3};
        public List<String> stringList = Arrays.asList("test1", "test2", "test3");
        public Test2 subObject = new Test2();
        public Map<String, String> mapTest = Map.of("testkey", "testvalue", "key2", "value2");

    }

    @EqualsAndHashCode
    static class Test2 {
        public String name = "Test2 field";
    }
}
