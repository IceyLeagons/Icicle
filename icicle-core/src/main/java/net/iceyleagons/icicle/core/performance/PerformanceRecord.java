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

package net.iceyleagons.icicle.core.performance;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * This class holds all the information about a section in the performance log.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Oct. 31, 2021
 */
@Getter
@RequiredArgsConstructor
public class PerformanceRecord {

    private final String name;
    private final long startMs;
    private final Class<?> clazz;
    private final ObjectLinkedOpenHashSet<PerformanceRecord> children = new ObjectLinkedOpenHashSet<>();

    @Setter
    private long endMs;
    @Setter
    private PerformanceRecord parent = null;

    /**
     * Creates a new record instance
     *
     * @param name the name of the section
     * @param clazz the class starting the section
     * @return the record
     */
    public static PerformanceRecord of(String name, Class<?> clazz) {
        return new PerformanceRecord(name, System.currentTimeMillis(), clazz);
    }

    /**
     * @return the time it took for the section to complete
     */
    public long getExecutionTime() {
        return endMs - startMs;
    }

    @Override
    public String toString() {
        return name + " [" + clazz.getName() + "] (took " + getExecutionTime() + " ms)";
    }
}
