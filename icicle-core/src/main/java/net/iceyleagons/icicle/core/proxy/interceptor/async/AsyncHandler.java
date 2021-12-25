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

    private final ExecutionHandler executionHandler;

    public AsyncHandler(ExecutionHandler executionHandler) {
        this.executionHandler = executionHandler;
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMatcher() {
        return ElementMatchers.isAnnotatedWith(Async.class).and(ElementMatchers.not(ElementMatchers.isAnnotatedWith(Sync.class)));
    }

    @Override
    public Implementation getImplementation() {
        return MethodDelegation.to(new AsyncDelegation(this.executionHandler));
    }
}
