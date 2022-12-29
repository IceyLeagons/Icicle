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

package net.iceyleagons.icicle.core.proxy.interceptor;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.QualifierKey;

import java.lang.reflect.Method;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 18, 2022
 */
@RequiredArgsConstructor
public class LazyMethodDelegation {

    private final BeanManager beanManager;
    private final QualifierKey qualifierKey;
    private volatile boolean resolved = false;

    @RuntimeType
    public Object run(@Origin Method superMethod, @AllArguments Object[] params) throws Exception {
        if (!resolved) {
            beanManager.getBeanRegistry().unregisterBean(qualifierKey.getClazz(), qualifierKey.getName()); // We want to unregister the proxied class, so later on it can be used instead of calling this proxy every time
            beanManager.createAndRegisterBean(qualifierKey.getClazz());
            resolved = true;
        }

        Object bean = beanManager.getBeanRegistry().getBeanNullable(qualifierKey);
        return superMethod.invoke(bean, params);
    }
}
