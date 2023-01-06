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

import com.google.common.base.Strings;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.Icicle;

import java.util.Map;

/**
 * PerformanceLog logs time it takes to execute a code section.
 * The {@link #begin(Application, String, Class)} and {@link #end(Application)} methods mark the boundary for a given section.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Oct. 31, 2021
 */
public class PerformanceLog {

    private static final long MS_THRESHOLD = 300;
    private static final Map<Application, PerformanceRecord> currentNodes = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>(4));

    /**
     * Marks a new section beginning.
     *
     * @param application the application holding the section
     * @param name the name of the section
     * @param clazz the class that calls this method
     */
    public static void begin(Application application, String name, Class<?> clazz) {
        if (!Icicle.PERFORMANCE_LOG) return;
        PerformanceRecord currentNode = currentNodes.get(application);

        PerformanceRecord executionRecord = PerformanceRecord.of(name, clazz);
        setParent(currentNode, executionRecord);
        currentNodes.put(application, executionRecord);
    }

    /**
     * Marks a section end. The section is automatically detected.
     *
     * @param application the application holding the section
     */
    public static void end(Application application) {
        if (!Icicle.PERFORMANCE_LOG) return;
        PerformanceRecord currentNode = currentNodes.get(application);

        if (currentNode == null) return;
        currentNode.setEndMs(System.currentTimeMillis());
        currentNodes.put(application, currentNode.getParent() != null ? currentNode.getParent() : currentNode);
    }

    /**
     * Returns the performance log of the given application
     *
     * @param application the application holding the log
     * @return the log
     */
    public static String dumpExecutionLog(Application application) {
        if (!Icicle.PERFORMANCE_LOG) return "";

        final StringBuilder stringBuilder = new StringBuilder("\n ====== [ Execution Log ] ======\n\n");

        dumpExecutionLog(currentNodes.get(application), 0, stringBuilder);
        stringBuilder.append("\n\n ============ [ x ] ============\n\n");

        return stringBuilder.toString();
    }

    /**
     * Utility method to construct the performance log.
     *
     * @param record the record
     * @param depth the current depth
     * @param sb the string builder used
     */
    private static void dumpExecutionLog(PerformanceRecord record, int depth, StringBuilder sb) {
        if (!Icicle.PERFORMANCE_LOG) return;
        String warning = (depth > 0 && record.getExecutionTime() >= MS_THRESHOLD) ? "[!]" : "   ";
        String prefix = warning + Strings.repeat("\t", depth) + (depth != 0 ? " -> " : "");

        sb.append(prefix).append(record.toString()).append("\n");

        int d = depth + 1;
        for (PerformanceRecord child : record.getChildren()) {
            dumpExecutionLog(child, d, sb);
        }
    }

    /**
     * Utility method to set parent of record
     *
     * @param parent the parent
     * @param child the child
     */
    private static void setParent(PerformanceRecord parent, PerformanceRecord child) {
        if (!Icicle.PERFORMANCE_LOG) return;
        child.setParent(parent);
        if (parent != null) {
            parent.getChildren().add(child);
        }
    }
}
