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

package net.iceyleagons.icicle.core.annotations.handlers.proxy;

import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation marks a bean as an MethodInterceptorHandler.
 * These beans extend the functionality of the {@link BeanProxyHandler} with new interceptor implementations.
 *
 * These classes must implement the {@link MethodInterceptorHandlerTemplate} interface.
 * In the life cycle of the applications, these beans get initialized after annotation handlers, but before all the other beans.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Nov. 14, 2021
 *
 * @see AutoCreate
 * @see BeanProxyHandler
 * @see MethodInterceptorHandlerTemplate
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface MethodInterceptionHandler {
}
