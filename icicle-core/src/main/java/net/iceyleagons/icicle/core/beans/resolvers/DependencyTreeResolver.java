package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;

import java.util.LinkedList;

public interface DependencyTreeResolver {

    LinkedList<Class<?>> resolveDependencyTree(Class<?> currentBean) throws CircularDependencyException;

}
