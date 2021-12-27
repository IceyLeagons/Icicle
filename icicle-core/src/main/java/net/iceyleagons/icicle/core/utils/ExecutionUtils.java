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

import net.iceyleagons.icicle.utilities.lang.Internal;

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
