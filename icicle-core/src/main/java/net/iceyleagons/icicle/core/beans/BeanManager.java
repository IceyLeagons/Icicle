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

package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.InjectionParameterResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.reflections.Reflections;

/**
 * A BeanManager is the core of all operations related to beans.
 * It is also the one, that creates the instances of
 * {@link BeanRegistry}, {@link DependencyTreeResolver}, {@link InjectionParameterResolver}
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public interface BeanManager {

    /**
     * Scans and creates the beans at startup.
     *
     * <b>WARNING!</b> Internal method: this method should only be called by an
     * {@link net.iceyleagons.icicle.core.Application} implementation, strictly at startup.
     *
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see Internal
     */
    @Internal
    void scanAndCreateBeans() throws Exception;

    /**
     * This method can be used if we want to create, auto-wire and register, etc. a bean after startup.
     * ({@link net.iceyleagons.icicle.core.annotations.AutoCreate} annotated types should never call this)
     *
     * @param beanClass the bean to create
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     */
    void createAndRegisterBean(Class<?> beanClass) throws Exception;

    /**
     * Cleans up the bean manager.
     * (Mainly calling other classes' cleanUp method)
     *
     * <b>WARNING!</b> Internal method: should only be called internally by the implementation of
     * {@link net.iceyleagons.icicle.core.Application} at shutdown.
     *
     * @see Internal
     */
    @Internal
    void cleanUp();

    /**
     * @return the {@link BeanRegistry} used by the manager
     */
    BeanRegistry getBeanRegistry();

    /**
     * @return the {@link DependencyTreeResolver} used by the manager
     */
    DependencyTreeResolver getDependencyTreeResolver();

    /**
     * @return the {@link InjectionParameterResolver} used by the manager
     */
    InjectionParameterResolver getConstructorParameterResolver();

    /**
     * @return the {@link Reflections} used by the manager
     */
    Reflections getReflectionsInstance();

    BeanProxyHandler getProxyHandler();
}
