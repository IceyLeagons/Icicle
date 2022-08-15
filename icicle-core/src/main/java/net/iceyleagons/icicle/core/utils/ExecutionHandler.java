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

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * Classes that implement this interface are responsible for the scheduling and parallelization of tasks.
 * Abstraction layer present for easier implementation in other environments.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sep. 25, 2021
 */
public interface ExecutionHandler {

    /**
     * Runs a task on the main thread.
     *
     * @param callable the task to be run.
     * @param <T>      return type.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runSync(Callable<T> callable);

    /**
     * Runs a task on the main thread with a predefined amount of delay.
     *
     * @param callable the task to be run.
     * @param <T>      return type.
     * @param timeUnit the time unit used to define the delay.
     * @param delay    the amount of delay in said time unit.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runSyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay);

    /**
     * Runs a task on the main thread with a predefined amount of delay every defined units of delay.
     *
     * @param callable   the task to be run.
     * @param <T>        return type.
     * @param periodUnit the time unit used to define the period delay.
     * @param period     the delay between executions in said time unit.
     * @param delayUnit  the time unit used to define the delay.
     * @param delay      the amount of delay in said time unit.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runSyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay);

    /**
     * Runs a task parallel to the main thread.
     *
     * @param callable the task to be run.
     * @param <T>      return type.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runAsync(Callable<T> callable);

    /**
     * Runs a task parallel to the main thread with a predefined amount of delay.
     *
     * @param callable the task to be run.
     * @param <T>      return type.
     * @param timeUnit the time unit used to define the delay.
     * @param delay    the amount of delay in said time unit.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runAsyncAfter(Callable<T> callable, TimeUnit timeUnit, long delay);

    /**
     * Runs a task parallel to the main thread with a predefined amount of delay every defined units of delay.
     *
     * @param callable   the task to be run.
     * @param <T>        return type.
     * @param periodUnit the time unit used to define the period delay.
     * @param period     the delay between executions in said time unit.
     * @param delayUnit  the time unit used to define the delay.
     * @param delay      the amount of delay in said time unit.
     * @return a {@link java.util.concurrent.CompletableFuture} instance that will contain the result of said operation.
     */
    <T> CompletableFuture<T> runAsyncPeriodically(Callable<T> callable, TimeUnit periodUnit, long period, TimeUnit delayUnit, long delay);

}
