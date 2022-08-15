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

package net.iceyleagons.icicle.core.utils;

import java.util.Map;

/**
 * Utility class to hold all the default values for all the primitive types to be used when autowiring.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public final class Defaults {

    public static final Map<Class<?>, Object> DEFAULT_TYPE_VALUES;
    public static final Map<String, Class<?>> CLASSES;

    static {
        DEFAULT_TYPE_VALUES = Map.of(
                boolean.class, false,
                byte.class, (byte) 0,
                short.class, (short) 0,
                int.class, 0,
                long.class, 0L,
                float.class, 0f,
                double.class, 0d,
                char.class, (char) 0);
        CLASSES = Map.of(
                "boolean", boolean.class,
                "byte", byte.class,
                "short", short.class,
                "int", int.class,
                "long", long.class,
                "float", float.class,
                "double", double.class,
                "char", char.class
        );
    }

    /**
     * @param name of the primitive (basically the keyword)
     * @return the class of the primitive
     */
    public static Class<?> getPrimitiveClassFromName(String name) {
        return CLASSES.getOrDefault(name.toLowerCase(), null);
    }
}
