package net.iceyleagons.icicle.core.performance;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;

@Getter
@RequiredArgsConstructor
public class ExecutionRecord {

    private final String name;
    private final long startMs;
    private final Class<?> clazz;
    private final LinkedHashSet<ExecutionRecord> children = new LinkedHashSet<>();

    @Setter
    private long endMs;
    @Setter
    private ExecutionRecord parent = null;

    public static ExecutionRecord of(String name, Class<?> clazz) {
        return new ExecutionRecord(name, System.currentTimeMillis(), clazz);
    }

    public long getExecutionTime() {
        return endMs - startMs;
    }

    @Override
    public String toString() {
        return name + " [" + clazz.getName() + "] (took " + getExecutionTime() + " ms)";
    }
}
