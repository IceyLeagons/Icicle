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

package net.iceyleagons.icicle.core.proxy.interceptor.bean;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.iceyleagons.icicle.core.beans.BeanRegistry;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 28, 2021
 */
@RequiredArgsConstructor
public class BeanDelegation {

    private final BeanRegistry beanRegistry;

    @RuntimeType
    public Object run(@SuperCall Callable<?> callable, @Origin Method method) throws Exception {
        Class<?> beanType = method.getReturnType();
        if (beanType.equals(Void.class)) {
            throw new IllegalStateException("Method marked with @Bean must have a non-void return type.");
        }

        if (this.beanRegistry.isRegistered(beanType)) {
            return this.beanRegistry.getBeanNullable(beanType);
        }

        Object bean = callable.call();
        this.beanRegistry.registerBean(beanType, bean);
        return bean;
    }
}
