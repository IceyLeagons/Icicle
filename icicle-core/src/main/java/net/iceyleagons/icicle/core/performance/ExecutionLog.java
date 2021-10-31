package net.iceyleagons.icicle.core.performance;

import com.google.common.base.Strings;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.Icicle;

import java.util.HashMap;
import java.util.Map;

public class ExecutionLog {

    private static final long MS_THRESHOLD = 300;
    private static Map<Application, ExecutionRecord> currentNodes = new HashMap<>();

    public static void begin(Application application, String name, Class<?> clazz) {
        if (!Icicle.PERFORMANCE_DEBUG) return;
        ExecutionRecord currentNode = currentNodes.get(application);

        ExecutionRecord executionRecord = ExecutionRecord.of(name, clazz);
        setParent(currentNode, executionRecord);
        currentNodes.put(application, executionRecord);
    }

    public static void end(Application application) {
        if (!Icicle.PERFORMANCE_DEBUG) return;
        ExecutionRecord currentNode = currentNodes.get(application);

        if (currentNode == null) return;
        currentNode.setEndMs(System.currentTimeMillis());
        currentNodes.put(application, currentNode.getParent() != null ? currentNode.getParent() : currentNode);
    }

    public static String dumpExecutionLog(Application application) {
        if (!Icicle.PERFORMANCE_DEBUG) return "";

        final StringBuilder stringBuilder = new StringBuilder("\n ====== [ Execution Log ] ======\n\n");

        dumpExecutionLog(currentNodes.get(application), 0, stringBuilder);
        stringBuilder.append("\n\n ============ [ x ] ============\n\n");

        return stringBuilder.toString();
    }

    private static void dumpExecutionLog(ExecutionRecord record, int depth, StringBuilder sb) {
        String warning = (depth > 1 && record.getExecutionTime() >= MS_THRESHOLD) ? "[!]" : "   ";
        String prefix = warning + Strings.repeat("\t", depth) + (depth != 0 ? " -> " : "");

        sb.append(prefix).append(record.toString()).append("\n");

        int d = depth + 1;
        for (ExecutionRecord child : record.getChildren()) {
            dumpExecutionLog(child, d, sb);
        }
    }

    private static void setParent(ExecutionRecord parent, ExecutionRecord child) {
        child.setParent(parent);
        if (parent != null) {
            parent.getChildren().add(child);
        }
    }
}
