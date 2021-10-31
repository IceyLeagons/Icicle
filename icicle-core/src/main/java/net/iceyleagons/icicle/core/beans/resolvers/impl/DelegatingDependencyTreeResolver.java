package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.performance.ExecutionLog;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.utilities.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Stack;

/**
 * Default implementation of {@link DependencyTreeResolver}.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 *
 * @see DependencyTreeResolver
 */
public class DelegatingDependencyTreeResolver implements DependencyTreeResolver {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingDependencyTreeResolver.class);
    private final BeanRegistry beanRegistry;

    public DelegatingDependencyTreeResolver(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }

    /**
     * Formats a human-friendly "graph" of the dependency circle.
     *
     * @param tree the dependencies that form a circle
     * @param start the starting point of the circle
     * @param end the ending point of the circle (the one that references the starting point --> making a circle)
     * @return the formatted "graph" to use in {@link CircularDependencyException}
     */
    private static String getCycleString(LinkedList<Class<?>> tree, Class<?> start, Class<?> end) {
        int startIndex = tree.indexOf(start);
        int endIndex = tree.indexOf(end);

        StringBuilder stringBuilder = new StringBuilder();


        stringBuilder.append("\n\t\t|-----|").append("\n\r");
        stringBuilder.append("\t\t|     |").append("\n\r");
        for (int i = startIndex; i <= endIndex; i++) {
            stringBuilder.append("\t\t|   ").append(tree.get(i).getName()).append("\n\r");
            stringBuilder.append("\t\t|     |").append("\n\r");
        }

        stringBuilder.append("\t\t|_____|").append("\n\r");

        return stringBuilder.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LinkedList<Class<?>> resolveDependencyTree(Class<?> currentBean) throws CircularDependencyException {
        logger.debug("Resolving dependency tree for bean-type: {}", currentBean.getName());

        LinkedList<Class<?>> tree = new LinkedList<>();
        Stack<Class<?>> stack = new Stack<>();

        tree.add(currentBean);
        stack.add(currentBean);

        while (!stack.isEmpty()) {
            Class<?> bean = stack.pop();
            if (beanRegistry.isRegistered(bean)) continue; //making sure it's already registered to not spend time

            Class<?>[] dependencies = BeanUtils.getResolvableConstructor(bean).getParameterTypes();
            for (Class<?> dependency : dependencies) {
                if (tree.contains(dependency)) {
                    logger.debug("Circular dependency found!");
                    throw new CircularDependencyException(getCycleString(tree, dependency, bean));
                }

                tree.add(dependency);
                stack.add(dependency);
            }
        }

        tree.remove(currentBean);

        return ListUtils.reverseLinkedList(tree);
    }
}
