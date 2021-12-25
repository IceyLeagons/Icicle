/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.serialization.annotations.Convert;
import net.iceyleagons.icicle.serialization.converters.builtin.UUIDConverter;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public class Test {

    public static void main(String[] args) throws BeanCreationException, UnsatisfiedDependencyException, CircularDependencyException {
        JsonSerializer json = new JsonSerializer(2);

        MapTest orig = new MapTest();
        String c = json.convertToString(orig);
        System.out.println(c);


        //System.out.println(new JsonSerializer().convertToString(new Test1()));
    }

    @EqualsAndHashCode
    static class MapTest {
        private final String test = "Hello";
        private final String[] test2 = new String[]{"Hello1", "Hello2"};
        private final Map<String, String> test3 = Map.of("hello", "hello value", "next-key", "next-key value");
        private final Map<String, Test2> test4 = Map.of("first", new Test2(), "second", new Test2());

        @Convert(UUIDConverter.class)
        private final UUID uuid = UUID.randomUUID();
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
        private final int[] array = new int[]{1, 2, 3};
    }

}
