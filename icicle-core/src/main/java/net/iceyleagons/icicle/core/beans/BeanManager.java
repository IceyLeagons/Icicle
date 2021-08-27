package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import org.reflections.Reflections;

public interface BeanManager {

    @Internal
    void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException;

    void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException;

    @Internal
    void cleanUp();

    BeanRegistry getBeanRegistry();
    DependencyTreeResolver getDependencyTreeResolver();
    ConstructorParameterResolver getConstructorParameterResolver();

    Reflections getReflectionsInstance();

}
