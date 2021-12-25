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

package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;

import java.lang.reflect.Constructor;

/**
 * A ConstructorParameterResolver is responsible for returning an array of parameters from the {@link BeanRegistry}
 * or via autowiring resolvers according to the constructor parameter needs.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public interface ConstructorParameterResolver {

    /**
     * Resolves the required parameters for bean-creation for the passed constructor.
     *
     * @param constructor  the constructor to use
     * @param beanRegistry the bean registry in charge
     * @return the array of parameters resolved (in the order of {@link Constructor#getParameterTypes()}
     * @throws UnsatisfiedDependencyException if a parameter's needs cannot be fulfilled.
     * @see BeanRegistry
     */
    Object[] resolveConstructorParameters(Constructor<?> constructor, BeanRegistry beanRegistry) throws UnsatisfiedDependencyException;

}
