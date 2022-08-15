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

import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;

import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * DependencyTreeResolvers are responsible for catching circular-dependency problems and returning the bean classes in
 * reverse order, so when creating the beans the one with the least amount of dependencies will be created first, then so on.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public interface DependencyTreeResolver {

    /**
     * Resolves the dependency tree from the passed bean.
     * <p>
     * Returns a LinkedList of all bean classes, the supplied bean is in relation with in a reversed order:
     * The bean with the least dependency should be the first element of the linked list.
     *
     * @param currentBean the bean to start from
     * @return the dependency tree
     * @throws CircularDependencyException if the dependencies in the tree form a circle somewhere
     * @throws UnsatisfiedDependencyException if a bean type is required, but no instance of it found inside the registry (and cannot be created)
     */
    LinkedList<Class<?>> resolveDependencyTree(Class<?> currentBean) throws CircularDependencyException, UnsatisfiedDependencyException;

    /**
     * Resolves the dependency tree from the passed bean.
     * <p>
     * Returns a LinkedList of all bean classes, the supplied bean is in relation with in a reversed order:
     * The bean with the least dependency should be the first element of the linked list.
     *
     * @param method the bean to start from (setter)
     * @return the dependency tree
     * @throws CircularDependencyException if the dependencies in the tree form a circle somewhere
     * @throws UnsatisfiedDependencyException if a bean type is required, but no instance of it found inside the registry (and cannot be created)
     */
    LinkedList<Class<?>> resolveDependencyTree(Method method) throws CircularDependencyException, UnsatisfiedDependencyException;
}
