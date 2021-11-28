package net.iceyleagons.icicle.core.proxy.interceptor.async;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.iceyleagons.icicle.core.annotations.execution.extra.After;
import net.iceyleagons.icicle.core.annotations.execution.extra.Periodically;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 */
@RequiredArgsConstructor
public class AsyncDelegation {

    private final ExecutionHandler executionHandler;

    @RuntimeType
    public Object run(@SuperCall Callable<?> callable, @Origin Method method) {
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
            return executionHandler.runAsyncPeriodically(callable, period.unit(), period.period(), delayUnit, delay).join();
        } else if (method.isAnnotationPresent(After.class)) {
            After after = method.getAnnotation(After.class);
            return executionHandler.runAsyncAfter(callable, after.unit(), after.delay()).join();
        }

        return executionHandler.runAsync(callable).join();
    }
}
