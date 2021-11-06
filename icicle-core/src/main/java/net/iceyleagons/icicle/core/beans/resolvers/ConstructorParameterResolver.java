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
