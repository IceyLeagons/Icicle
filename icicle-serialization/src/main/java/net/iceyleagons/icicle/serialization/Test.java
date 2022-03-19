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
import net.iceyleagons.icicle.serialization.serializers.impl.BsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.impl.YamlSerializer;
import net.iceyleagons.icicle.utilities.Benchmark;
import org.json.JSONObject;
import org.simpleyaml.configuration.file.YamlFile;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
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
        JsonSerializer jsonSerializer = new JsonSerializer();
        ObjectMapper mapper = new ObjectMapper();

        Test1 original = new Test1();
        Benchmark.run(() -> {
            new YamlSerializer().serializeToPath(original, new File("test.yml").toPath());
            return null;
        }, "serialization");



/*
        System.out.println(original.test + " <--> " + demap.test);
        System.out.println(Arrays.toString(original.test2) + " <--> " + Arrays.toString(demap.test2));
        System.out.println(original.test3 + " <--> " + demap.test3);
        System.out.println(Arrays.toString(original.test4) + " <--> " + Arrays.toString(demap.test4));
        System.out.println(Arrays.toString(original.test5.toArray(String[]::new)) + " <--> " + Arrays.toString(demap.test5.toArray(String[]::new)));
        System.out.println(original.test6.test2 + " <--> " + demap.test6.test2);
        System.out.println(Arrays.toString(original.test7.entrySet().toArray()) + " <--> " + Arrays.toString(demap.test7.entrySet().toArray()));

        System.out.println();
        System.out.println("Serialized: ");
        System.out.println(serialized.toString(2));

 */
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
