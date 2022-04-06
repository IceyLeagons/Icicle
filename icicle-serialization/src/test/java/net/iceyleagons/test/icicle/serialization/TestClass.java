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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 06, 2022
 */
public class TestClass {

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
