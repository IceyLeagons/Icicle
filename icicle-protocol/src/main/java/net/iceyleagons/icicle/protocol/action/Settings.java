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

package net.iceyleagons.icicle.protocol.action;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;

import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 31, 2022
 */
@Getter
public class Settings {
    // TARGET - 0
    public static final int TARGET = 0;

    // POSITIONAL - 1xx
    public static final int POSITION = 100;
    public static final int CENTER = 101;
    public static final int SIZE = 103;

    // ENTITY - 2xx
    public static final int ENTITY = 200;
    public static final int ORIGIN = 201;

    // ARGUMENT - 3xx
    public static final int MATERIAL = 300;
    public static final int LENGTH = 301;

    private final Map<Integer, Object> settings = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>(4));

    public static Settings create() {
        return new Settings();
    }

    public Settings with(int id, Object value) {
        settings.put(id, value);
        return this;
    }
}
