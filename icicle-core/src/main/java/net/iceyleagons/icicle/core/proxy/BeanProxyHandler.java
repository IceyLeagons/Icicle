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

package net.iceyleagons.icicle.core.proxy;

import net.bytebuddy.ByteBuddy;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;

import java.lang.reflect.Constructor;
import java.util.Set;

/**
 * The proxy handler is responsible for handling the AOP part of Icicle and enabling many fancy features that normally could only be
 * achieved via compiler plugins.
 * <br><br>
 * As of now, only {@link ByteBuddy} is available as a proxying solution, so we did not implement an abstraction layer, and all of our
 * proxy related classes import things from ByteBuddy.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 26, 2021
 */
public interface BeanProxyHandler {

    /**
     * Called mostly be the {@link net.iceyleagons.icicle.core.beans.BeanManager}, this method is responsible for creating
     * the proxied beans to enable many amazing features for Icicle.
     * This calls {@link #getEnhancedBean(Constructor)} then constructs the bean with the supplied arguments using the constructor returned.
     *
     * @param constructor the constructor to use
     * @param arguments the arguments for the constructor
     * @return the enhanced bean
     * @param <T> the bean type
     * @throws BeanCreationException if the bean cannot be created due to any underlying error
     */
    <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments) throws BeanCreationException;

    /**
     * Responsible for the actual proxying.
     *
     * @param constructor the constructor to use
     * @return a {@link Constructor} to be used to create the enhanced bean. (parameters are identical to the given constructor)
     * @param <T> the bean type
     * @throws BeanCreationException if the bean cannot be created due to any underlying error
     */
    <T> Constructor<T> getEnhancedBean(Constructor<T> constructor) throws BeanCreationException;

    /**
     * @return the registered {@link MethodAdviceHandlerTemplate}s.
     */
    Set<MethodAdviceHandlerTemplate> getMethodAdviceHandlers();

    /**
     * Registers a new {@link MethodAdviceHandlerTemplate}.
     *
     * @param adviceHandler the handler to register
     */
    void registerAdviceTemplate(MethodAdviceHandlerTemplate adviceHandler);

    /**
     * Registers a new {@link MethodInterceptorHandlerTemplate}
     *
     * @param interceptorTemplate the handler to register
     */
    void registerInterceptorTemplate(MethodInterceptorHandlerTemplate interceptorTemplate);

    /**
     * @return the {@link ByteBuddy} instance used.
     */
    ByteBuddy getProxy();
}
