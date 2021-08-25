package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.Set;

public class DefaultBeanManager implements BeanManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanManager.class);

    private final BeanRegistry beanRegistry;
    private final DependencyTreeResolver dependencyTreeResolver;
    private final ConstructorParameterResolver constructorParameterResolver;
    private final Reflections reflections;

    private final MergedAnnotationResolver autoCreationAnnotationResolver;

    public DefaultBeanManager(Reflections reflections) {
        this.reflections = reflections;

        this.beanRegistry = new DelegatingBeanRegistry();
        this.dependencyTreeResolver = new DelegatingDependencyTreeResolver();
        this.constructorParameterResolver = new DelegatingConstructorParameterResolver();

        this.autoCreationAnnotationResolver = new MergedAnnotationResolver(AutoCreate.class, reflections);
    }

    @Override
    public void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> autoCreationTypes = autoCreationAnnotationResolver.getAllTypesAnnotated();

        for (Class<?> autoCreationType : autoCreationTypes) {
            createAndRegisterBean(autoCreationType);
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
    public Reflections getReflectionsInstance() {
        return this.reflections;
    }

    @Override
    public void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException {
        if (!beanRegistry.isRegistered(beanClass)) {
            Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

            if (constructor.getParameterTypes().length == 0) {
                Object bean = BeanUtils.instantiateClass(constructor);
                beanRegistry.registerBean(beanClass, bean);
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
}
