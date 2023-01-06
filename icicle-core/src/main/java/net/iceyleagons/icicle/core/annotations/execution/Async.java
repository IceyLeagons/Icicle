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

package net.iceyleagons.icicle.core.annotations.execution;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Methods annotated with this annotation will be called asynchronously via {@link net.iceyleagons.icicle.core.utils.ExecutionHandler}.
 * This annotation can only be used inside Icicle managed beans.
 * <p>
 * Functionality is implemented at {@link net.iceyleagons.icicle.core.proxy.interceptor.async.AsyncHandler}
 * Can be used in conjunction with annotations defined inside the package: <b>net.iceyleagons.icicle.core.annotation.execution.extra</b>.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @see net.iceyleagons.icicle.core.utils.ExecutionHandler
 * @see net.iceyleagons.icicle.core.proxy.interceptor.async.AsyncHandler
 * @since Aug. 24, 2021
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Async {

    /**
     * <b>WARNING!</b> Setting blocking to true and calling the method from the main thread
     * is not recommended in Bukkit environments, as it may/will cause the server to freeze up.
     * <br><br>
     * <b>In general we don't recommend setting this to true, due the the following reason:</b>
     * <br><br>
     * <p>
     * We recommend using a Consumer in the parameters and passing the value through that, and using it a non-blocking way.
     * Even though returns are supported, we highly recommend using the method above for non-blocking as well. If you prefer not to, then:
     * If the method is executed in a non-blocking way, the return type must be an Object.
     * Reason is that we call the original method (and it should return the proper value), but the proxy will return a CompletableFuture (totally different, than your return type),
     * so where the method is called you need to cast it to CompletableFuture. As we don't have a compiler plugin just yet, this is a workaround, but will result in warnings, so hopefully you get why we
     * recommend the consumer solution.
     * </p>
     *
     * @return true if the method should be run async in a blocking way, or false to not join in.
     * @deprecated should not be set to true, you've been warned. Use consumers in parameters instead!
     */
    @Deprecated
    boolean blocking() default false;
}
