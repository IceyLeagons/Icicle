package net.iceyleagons.icicle.core.proxy.interceptor.async;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.execution.Async;
import net.iceyleagons.icicle.core.annotations.execution.Sync;
import net.iceyleagons.icicle.core.annotations.handlers.proxy.MethodInterceptionHandler;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 */
@MethodInterceptionHandler
public class AsyncHandler implements MethodInterceptorHandlerTemplate {

    public AsyncHandler(ExecutionHandler executionHandler) {
        AsyncDelegation.setupHandler(executionHandler);
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMatcher() {
        return ElementMatchers.isAnnotatedWith(Async.class).and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(Sync.class)));
    }

    @Override
    public Implementation getImplementation() {
        return MethodDelegation.to(new AsyncDelegation());
    }
}
