package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;

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
     */
    LinkedList<Class<?>> resolveDependencyTree(Class<?> currentBean) throws CircularDependencyException;
}
