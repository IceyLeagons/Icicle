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

package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.other.QualifierKey;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.utilities.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/**
 * Default implementation of {@link DependencyTreeResolver}.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @see DependencyTreeResolver
 * @since Aug. 23, 2021
 */
public class DelegatingDependencyTreeResolver implements DependencyTreeResolver {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingDependencyTreeResolver.class);
    private final BeanRegistry beanRegistry;
    private final AutowiringAnnotationResolver autowiringAnnotationResolver;
    private final MergedAnnotationResolver autoCreateResolver;

    public DelegatingDependencyTreeResolver(BeanRegistry beanRegistry, AutowiringAnnotationResolver autowiringAnnotationResolver, MergedAnnotationResolver autoCreateResolver) {
        this.beanRegistry = beanRegistry;
        this.autoCreateResolver = autoCreateResolver;
        this.autowiringAnnotationResolver = autowiringAnnotationResolver;
    }

    /**
     * Formats a human-friendly "graph" of the dependency circle.
     *
     * @param rawTree the dependencies that form a circle
     * @param start   the starting point of the circle
     * @param end     the ending point of the circle (the one that references the starting point --> making a circle)
     * @return the formatted "graph" to use in {@link CircularDependencyException}
     */
    private static String getCycleString(LinkedList<QualifierKey> rawTree, Class<?> start, Class<?> end) {
        LinkedList<Class<?>> tree = rawTree.stream().map(QualifierKey::getClazz).collect(Collectors.toCollection(LinkedList::new));

        int startIndex = tree.indexOf(start);
        int endIndex = tree.indexOf(end);

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("\n\t\t┌─────┐").append("\n\r");
        stringBuilder.append("\t\t│     ↓").append("\n\r");
        for (int i = startIndex; i <= endIndex; i++) {
            stringBuilder.append("\t\t│   ").append(tree.get(i).getName()).append("\n\r");
            stringBuilder.append("\t\t│     ↓").append("\n\r");
        }

        stringBuilder.append("\t\t└─────┘").append("\n\r");

        return stringBuilder.toString();
    }

    private void handleDependencies(Parameter[] dependencies, Stack<Class<?>> stack, LinkedList<QualifierKey> tree, Class<?> bean) throws UnsatisfiedDependencyException, CircularDependencyException {
        x:
        for (Parameter param : dependencies) {
            Class<?> dependency = param.getType();
            for (Annotation annotation : param.getAnnotations()) {
                if (autowiringAnnotationResolver.has(annotation.annotationType())) {
                    continue x;
                }
            }

            String qualifier = QualifierKey.getQualifier(param);

            if (!beanRegistry.isRegistered(dependency, qualifier)) {
                if (tree.contains(new QualifierKey(dependency, qualifier))) {
                    logger.warn("Circular dependency found!");
                    throw new CircularDependencyException(getCycleString(tree, dependency, bean));
                } else if (!this.autoCreateResolver.isAnnotated(dependency)) {
                    if (dependency.isInterface()) {
                        // Maybe it's a Service or GlobalService interface
                        List<Class<?>> impls =
                                BeanUtils.getImplementationsOfInterface(dependency, this.autoCreateResolver.getReflections())
                                        .stream().filter(c -> QualifierKey.getQualifier(c).equals(qualifier)).collect(Collectors.toList());

                        if (impls.isEmpty()) {
                            throw new UnsatisfiedDependencyException(param);
                        }

                        if (impls.size() > 1) {
                            throw new IllegalStateException("Multiple implementations are found for type: " + dependency.getName());
                        }

                        final Class<?> impl = impls.get(0);
                        tree.add(new QualifierKey(impl, QualifierKey.getQualifier(impl)));
                        stack.add(impl);
                        continue;
                    } else {
                        throw new UnsatisfiedDependencyException(param);
                    }
                }
            }

            tree.add(new QualifierKey(dependency, qualifier));
            stack.add(dependency);
        }
    }

    @Override
    public LinkedList<Class<?>> resolveDependencyTree(Method method) throws CircularDependencyException, UnsatisfiedDependencyException {
        logger.debug("Resolving dependency tree for bean-type: {}", method.getDeclaringClass().getName());

        final LinkedList<QualifierKey> tree = new LinkedList<>();
        final Stack<Class<?>> stack = new Stack<>();
        final Parameter[] params = method.getParameters();

        handleDependencies(params, stack, tree, method.getDeclaringClass());
        resolveDependencyTreeForBean(tree, stack);

        return ListUtils.reverseLinkedList(tree)
                .stream().map(QualifierKey::getClazz).collect(Collectors.toCollection(LinkedList::new));
    }

    private void resolveDependencyTreeForBean(LinkedList<QualifierKey> tree, Stack<Class<?>> stack) throws UnsatisfiedDependencyException, CircularDependencyException {
        while (!stack.isEmpty()) {
            Class<?> bean = stack.pop();
            if (beanRegistry.isRegistered(bean, QualifierKey.getQualifier(bean)) || bean.isInterface())
                continue; //making sure it's already registered to not spend time

            Parameter[] dependencies = BeanUtils.getResolvableConstructor(bean).getParameters();
            handleDependencies(dependencies, stack, tree, bean);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedList<Class<?>> resolveDependencyTree(Class<?> currentBean) throws CircularDependencyException, UnsatisfiedDependencyException {
        logger.debug("Resolving dependency tree for bean-type: {}", currentBean.getName());

        final LinkedList<QualifierKey> tree = new LinkedList<>();
        final Stack<Class<?>> stack = new Stack<>();

        QualifierKey cq = new QualifierKey(currentBean, QualifierKey.getQualifier(currentBean));
        tree.add(cq);
        stack.add(currentBean);

        resolveDependencyTreeForBean(tree, stack);
        tree.remove(cq);

        return ListUtils.reverseLinkedList(tree)
                .stream().map(QualifierKey::getClazz).collect(Collectors.toCollection(LinkedList::new));
    }
}
