package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DefaultBeanManager implements BeanManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanManager.class);

    private final BeanRegistry beanRegistry;
    private final DependencyTreeResolver dependencyTreeResolver;
    private final ConstructorParameterResolver constructorParameterResolver;
    private final Reflections reflections;
    private final MergedAnnotationResolver autoCreationAnnotationResolver;
    private final Application application;

    public DefaultBeanManager(Application application) {
        this.application = application;
        this.reflections = application.getReflections();

        this.beanRegistry = new DelegatingBeanRegistry();
        this.dependencyTreeResolver = new DelegatingDependencyTreeResolver();
        this.constructorParameterResolver = new DelegatingConstructorParameterResolver();

        this.autoCreationAnnotationResolver = new MergedAnnotationResolver(AutoCreate.class, reflections);

    }

    private void createConfigs(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException {
        Iterator<Class<?>> iterator = autoCreationTypes.iterator(); //creating an iterator to avoid concurrent modification

        while (iterator.hasNext()) {
            Class<?> autoCreationType = iterator.next();

            if (!autoCreationType.isAnnotationPresent(Config.class)) continue;
            iterator.remove(); //removing the config from the types to not iterate over the afterwards


            createAndRegisterBean(autoCreationType);
            Object object = this.beanRegistry.getBeanNullable(autoCreationType);

            if (!(object instanceof Configuration)) {
                LOGGER.warn("Config described by {} does not extend any Configuration instance. (Did you forget to extend AbstractConfiguration?)", autoCreationType);
                this.beanRegistry.unregisterBean(autoCreationType);
                continue;
            }

            Configuration configuration = (Configuration) object;
            Config annotation = autoCreationType.getAnnotation(Config.class);

            configuration.setConfigFile(new AdvancedFile(new File(this.application.getConfigurationEnvironment().getConfigRootFolder(), annotation.value())));

            configuration.setOrigin(object);
            configuration.setOriginType(autoCreationType);

            if (annotation.headerLines().length != 0) {
                configuration.setHeader(String.join("\n", annotation.headerLines()));
            }


            this.application.getConfigurationEnvironment().addConfiguration(configuration);
        }
    }

    @Override
    public void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> autoCreationTypes = this.autoCreationAnnotationResolver.getAllTypesAnnotated();

        //First we want to create all the configurations because other beans may need them during construction
        createConfigs(autoCreationTypes);

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
        if (!this.beanRegistry.isRegistered(beanClass)) {
            LOGGER.debug("Creating and registering bean of type: {}", beanClass.getName());

            Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

            if (constructor.getParameterTypes().length == 0) {
                Object bean = BeanUtils.instantiateClass(constructor, null);
                this.beanRegistry.registerBean(beanClass, bean);
                return;
            } else {
                LinkedList<Class<?>> dependencies = this.dependencyTreeResolver.resolveDependencyTree(beanClass);

                LOGGER.debug("Found {} dependencies for bean of type {}", dependencies.size(), beanClass.getName());
                for (Class<?> dependency : dependencies) {
                    createAndRegisterBean(dependency);
                }
            }

            Object[] parameters = this.constructorParameterResolver.resolveConstructorParameters(constructor, getBeanRegistry());
            this.beanRegistry.registerBean(beanClass, BeanUtils.instantiateClass(constructor, null, parameters));
        }
    }
}
