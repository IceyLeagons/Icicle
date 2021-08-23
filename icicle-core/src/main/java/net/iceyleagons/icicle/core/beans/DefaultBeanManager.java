package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;

public class DefaultBeanManager implements BeanManager {

    private final BeanRegistry beanRegistry;
    private final DependencyTreeResolver dependencyTreeResolver;
    private final ConstructorParameterResolver constructorParameterResolver;

    public DefaultBeanManager() {
        this.beanRegistry = new DelegatingBeanRegistry();
        this.dependencyTreeResolver = new DelegatingDependencyTreeResolver();
        this.constructorParameterResolver = new DelegatingConstructorParameterResolver();
    }

    @Override
    public void scanAndCreateBeans(Reflections reflections) throws BeanCreationException, CircularDependencyException {
        Set<Class<? extends Annotation>> annotations = getAutoCreationAnnotations();

        for (Class<? extends Annotation> annotation : annotations) {
            for (Class<?> bean : reflections.getTypesAnnotatedWith(annotation)) {
                createAndRegisterBean(bean);
            }
        }
    }

    @Override
    public BeanRegistry getBeanRegistry() {
        return this.beanRegistry;
    }

    @Override
    public DependencyTreeResolver getDependencyTreeResolver() {
        return this.dependencyTreeResolver;
    }

    @Override
    public ConstructorParameterResolver getConstructorParameterResolver() {
        return this.constructorParameterResolver;
    }

    @Override
    public void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException {
        if (!beanRegistry.isRegistered(beanClass)) {
            Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

            if (constructor.getParameterTypes().length == 0) {
                beanRegistry.registerBean(beanClass, BeanUtils.instantiateClass(constructor));
                return;
            } else {
                LinkedList<Class<?>> dependencies = dependencyTreeResolver.resolveDependencyTree(beanClass);

                for (Class<?> dependency : dependencies) {
                    createAndRegisterBean(dependency);
                }
            }

            Object[] parameters = constructorParameterResolver.resolveConstructorParameters(constructor, getBeanRegistry());
            beanRegistry.registerBean(beanClass, BeanUtils.instantiateClass(constructor, parameters));
        }
    }

    private Set<Class<? extends Annotation>> getAutoCreationAnnotations() {
        return Collections.emptySet();
    }
}
