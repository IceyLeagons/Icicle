package net.iceyleagons.icicle.core.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public interface ExecutionHandler {

    <T> CompletableFuture<T> runSync(Callable<T> callable);

    <T> CompletableFuture<T> runSyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay);

    <T> CompletableFuture<T> runSyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay);

    <T> CompletableFuture<T> runAsync(Callable<T> callable);

    <T> CompletableFuture<T> runAsyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay);

    <T> CompletableFuture<T> runAsyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay);
}
