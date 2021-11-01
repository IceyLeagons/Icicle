package net.iceyleagons.icicle.core.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;

/**
 * @since Oct. 31, 2021
 */
@Getter
@RequiredArgsConstructor
public class PerformanceRecord {

    private final String name;
    private final long startMs;
    private final Class<?> clazz;
    private final LinkedHashSet<PerformanceRecord> children = new LinkedHashSet<>();

    @Setter
    private long endMs;
    @Setter
    private PerformanceRecord parent = null;

    public static PerformanceRecord of(String name, Class<?> clazz) {
        return new PerformanceRecord(name, System.currentTimeMillis(), clazz);
    }

    public long getExecutionTime() {
        return endMs - startMs;
    }

    @Override
    public String toString() {
        return name + " [" + clazz.getName() + "] (took " + getExecutionTime() + " ms)";
    }
}
