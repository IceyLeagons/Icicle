package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import org.reflections.Reflections;

public interface BeanManager {

    void scanAndCreateBeans(Reflections reflections) throws BeanCreationException, CircularDependencyException;
    void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException;

    BeanRegistry getBeanRegistry();
    DependencyTreeResolver getDependencyTreeResolver();
    ConstructorParameterResolver getConstructorParameterResolver();

}
