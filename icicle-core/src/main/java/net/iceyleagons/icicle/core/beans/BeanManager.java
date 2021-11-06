package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import org.reflections.Reflections;

/**
 * A BeanManager is the core of all operations related to beans.
 * It is also the one, that creates the instances of
 * {@link BeanRegistry}, {@link DependencyTreeResolver}, {@link ConstructorParameterResolver}
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
    void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException;

    /**
     * This method can be used if we want to create, auto-wire and register, etc. a bean after startup.
     * ({@link net.iceyleagons.icicle.core.annotations.AutoCreate} annotated types should never call this)
     *
     * @param beanClass the bean to create
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     */
    void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException;

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
     * @return the {@link ConstructorParameterResolver} used by the manager
     */
    ConstructorParameterResolver getConstructorParameterResolver();

    /**
     * @return the {@link Reflections} used by the manager
     */
    Reflections getReflectionsInstance();
}
