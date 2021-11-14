package net.iceyleagons.icicle.core.utils;

import net.iceyleagons.icicle.core.annotations.Internal;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class is intended for debug purposes only!!!
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 * @deprecated DEBUG purposes only, marked as deprecated as a warning.
 */
@Internal
@Deprecated
public final class ExecutionUtils {

    /**
     * A debug execution handler does not alter the execution type, but only prints out debug messages.
     *
     * @return an empty execution handler for debugging.
     */
    @Internal
    public static ExecutionHandler debugHandler() {
        return new ExecutionHandler() {
            @Override
            public <T> CompletableFuture<T> runSync(Callable<T> callable) {
                System.out.println("Running sync");
                return getFrom(callable);
            }

            @Override
            public <T> CompletableFuture<T> runSyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
                System.out.println("Running sync after");
                return getFrom(callable);
            }

            @Override
            public <T> CompletableFuture<T> runSyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
                System.out.println("Running sync periodically");
                return getFrom(callable);
            }

            @Override
            public <T> CompletableFuture<T> runAsync(Callable<T> callable) {
                System.out.println("Running async");
                return getFrom(callable);
            }

            @Override
            public <T> CompletableFuture<T> runAsyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
                System.out.println("Running async after");
                return getFrom(callable);
            }

            @Override
            public <T> CompletableFuture<T> runAsyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
                System.out.println("Running async periodically");
                return getFrom(callable);
            }
        };
    }

    private static <T> CompletableFuture<T> getFrom(Callable<T> callable) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        try {
            completableFuture.complete(callable.call());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return completableFuture;
    }
}
