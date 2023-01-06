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

package net.iceyleagons.icicle.core.standalone;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;

import java.util.concurrent.*;

/**
 * Implementation of {@link ExecutionHandler} for the JVM used for the standalone application of Icicle.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 24, 2022
 */
public class StandaloneExecutionHandler implements ExecutionHandler {

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(4);

    @Override
    @SneakyThrows
    public <T> CompletableFuture<T> runSync(Callable<T> callable) {
        return CompletableFuture.completedFuture(callable.call());
    }

    @Override
    @SneakyThrows
    public <T> CompletableFuture<T> runSyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
        Thread.sleep(timeUnit.toMillis(delay));
        return CompletableFuture.completedFuture(callable.call());
    }

    @Override
    public <T> CompletableFuture<T> runSyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                T result = callable.call();
                if (!completableFuture.isDone()) {
                    completableFuture.complete(result);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, delayUnit.toMillis(delay), periodUnit.toMillis(period), TimeUnit.MILLISECONDS);
        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsync(Callable<T> callable) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduledExecutorService.submit(() -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduledExecutorService.schedule(() -> {
            try {
                completableFuture.complete(callable.call());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, delay, timeUnit);

        return completableFuture;
    }

    @Override
    public <T> CompletableFuture<T> runAsyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay) {
        CompletableFuture<T> completableFuture = new CompletableFuture<>();
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                T result = callable.call();
                if (!completableFuture.isDone()) {
                    completableFuture.complete(result);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, delayUnit.toMillis(delay), periodUnit.toMillis(period), TimeUnit.MILLISECONDS);
        return completableFuture;
    }
}
