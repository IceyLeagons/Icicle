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

package net.iceyleagons.icicle.core.proxy.interceptor.sync;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.iceyleagons.icicle.core.annotations.execution.Sync;
import net.iceyleagons.icicle.core.annotations.execution.extra.After;
import net.iceyleagons.icicle.core.annotations.execution.extra.Periodically;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Actual implementation of the sync proxy logic.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 */
@RequiredArgsConstructor
public class SyncDelegation {

    private final ExecutionHandler executionHandler;

    @RuntimeType
    public Object run(@SuperCall Callable<?> callable, @Origin Method method) throws ExecutionException, InterruptedException {
        CompletableFuture<?> future;
        if (method.isAnnotationPresent(Periodically.class)) {
            final Periodically period = method.getAnnotation(Periodically.class);

            long delay = 0;
            TimeUnit delayUnit = TimeUnit.SECONDS;
            if (method.isAnnotationPresent(After.class)) {
                After after = method.getAnnotation(After.class);
                delay = after.delay();
                delayUnit = after.unit();
            }

            //periodical execution only returns the first time it gets run
            future = executionHandler.runSyncPeriodically(callable, period.unit(), period.period(), delayUnit, delay);
        } else if (method.isAnnotationPresent(After.class)) {
            After after = method.getAnnotation(After.class);
            future = executionHandler.runSyncAfter(callable, after.unit(), after.delay());
        } else {
            future = executionHandler.runSync(callable);
        }

        Class<?> type = method.getReturnType();
        if (type.equals(Void.class)) return null;


        boolean blocking = method.getAnnotation(Sync.class).blocking();
        if (blocking) {
            if (future.isDone()) {
                return ReflectionUtils.castIfNecessary(type, future.get());
            }

            Object result = future.join(); // This may cause server to lock up, see javadoc of @Sync
            return ReflectionUtils.castIfNecessary(type, result);
        }

        return ReflectionUtils.castIfNecessary(type, future);
    }
}
