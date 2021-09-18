package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.MethodInterceptor;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.CustomAutoCreateAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingAutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingConstructorParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingCustomAutoCreateAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DefaultBeanManager implements BeanManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultBeanManager.class);

    private final BeanRegistry beanRegistry;
    private final DependencyTreeResolver dependencyTreeResolver;
    private final ConstructorParameterResolver constructorParameterResolver;
    private final Reflections reflections;

    private final BeanProxyHandler beanProxyHandler;

    private final MergedAnnotationResolver autoCreationAnnotationResolver;

    private final AutowiringAnnotationResolver autowiringAnnotationResolver;
    private final CustomAutoCreateAnnotationResolver customAutoCreateAnnotationResolver;

    private final Application application;

    public DefaultBeanManager(Application application) {
        this.application = application;
        this.reflections = application.getReflections();

        this.beanRegistry = new DelegatingBeanRegistry();
        this.dependencyTreeResolver = new DelegatingDependencyTreeResolver(this.beanRegistry);

        this.beanProxyHandler = new ByteBuddyProxyHandler();

        this.autowiringAnnotationResolver = new DelegatingAutowiringAnnotationResolver();
        this.customAutoCreateAnnotationResolver = new DelegatingCustomAutoCreateAnnotationResolver();

        this.constructorParameterResolver = new DelegatingConstructorParameterResolver(autowiringAnnotationResolver);

        this.autoCreationAnnotationResolver = new MergedAnnotationResolver(AutoCreate.class, reflections);
    }

    private static Set<Class<?>> getAndRemoveTypesAnnotatedWith(Class<? extends Annotation> annotation, Set<Class<?>> autoCreationTypes) {
        Iterator<Class<?>> iterator = autoCreationTypes.iterator(); //creating an iterator to avoid concurrent modification
        Set<Class<?>> result = new HashSet<>();


        while (iterator.hasNext()) {
            Class<?> autoCreationType = iterator.next();

            if (autoCreationType.isAnnotationPresent(annotation)) {
                result.add(autoCreationType);
                iterator.remove();
            }
        }

        return result;
    }

    private void createConfigs(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> configs = getAndRemoveTypesAnnotatedWith(Config.class, autoCreationTypes);

        for (Class<?> config : configs) {
            createAndRegisterBean(config);
            Object object = this.beanRegistry.getBeanNullable(config);

            if (!(object instanceof Configuration)) {
                LOGGER.warn("Config described by {} does not extend any Configuration instance. (Did you forget to extend AbstractConfiguration?)", config.getName());
                this.beanRegistry.unregisterBean(config);
                continue;
            }

            Configuration configuration = (Configuration) object;
            Config annotation = config.getAnnotation(Config.class);

            configuration.setConfigFile(new AdvancedFile(new File(this.application.getConfigurationEnvironment().getConfigRootFolder(), annotation.value())));

            configuration.setOrigin(object);
            configuration.setOriginType(config);

            if (annotation.headerLines().length != 0) {
                configuration.setHeader(String.join("\n", annotation.headerLines()));
            }

            configuration.afterConstruct();
            this.application.getConfigurationEnvironment().addConfiguration(configuration);
        }

        this.application.getConfigurationEnvironment().updateValues();
    }

    private void createAndRegisterMethodInterceptors(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> interceptors = getAndRemoveTypesAnnotatedWith(MethodInterceptor.class, autoCreationTypes);

        for (Class<?> interceptor : interceptors) {
            createAndRegisterBean(interceptor);
            Object object = this.beanRegistry.getBeanNullable(interceptor);

            if (object instanceof net.iceyleagons.icicle.core.proxy.MethodInterceptor) {
                this.beanProxyHandler.registerInterceptor((net.iceyleagons.icicle.core.proxy.MethodInterceptor) object);
            }
        }
    }

    private void createAnnotationHandlers(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> handlers = getAndRemoveTypesAnnotatedWith(AnnotationHandler.class, autoCreationTypes);

        for (Class<?> handler : handlers) {
            createAndRegisterBean(handler);
            Object object = this.beanRegistry.getBeanNullable(handler);

            if (object instanceof AutowiringAnnotationHandler) {
                this.autowiringAnnotationResolver.registerAutowiringAnnotationHandler((AutowiringAnnotationHandler) object);
            } else if (object instanceof CustomAutoCreateAnnotationHandler) {
                this.customAutoCreateAnnotationResolver.registerCustomAutoCreateAnnotationHandler((CustomAutoCreateAnnotationHandler) object);
            }
        }
    }

    @Override
    public void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException {
        Set<Class<?>> autoCreationTypes = this.autoCreationAnnotationResolver.getAllTypesAnnotated();

        //First we want to create all the configurations because other beans may need them during construction
        createConfigs(autoCreationTypes);

        //Second we want to register all autowiring annotation handlers before creating beans
        createAnnotationHandlers(autoCreationTypes);

        createAndRegisterMethodInterceptors(autoCreationTypes);

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
        if (beanClass == String.class || beanClass.isPrimitive()) return;

        if (!this.beanRegistry.isRegistered(beanClass)) { //this is here because a class may have multiple auto-create annotations and also just a precaution
            LOGGER.debug("Creating and registering bean of type: {}", beanClass.getName());

            Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

            if (constructor.getParameterTypes().length == 0) {
                Object bean = BeanUtils.instantiateClass(constructor, this.beanProxyHandler);
                this.registerBean(beanClass, bean);
                return;
            } else {
                LinkedList<Class<?>> dependencies = this.dependencyTreeResolver.resolveDependencyTree(beanClass);

                LOGGER.debug("Found {} dependencies for bean of type {}", dependencies.size(), beanClass.getName());
                for (Class<?> dependency : dependencies) {
                    createAndRegisterBean(dependency);
                }
            }

            Object[] parameters = this.constructorParameterResolver.resolveConstructorParameters(constructor, getBeanRegistry());
            this.registerBean(beanClass, BeanUtils.instantiateClass(constructor, this.beanProxyHandler, parameters));
        }
    }

    private void registerBean(Class<?> beanClass, Object bean) {
        this.beanRegistry.registerBean(beanClass, bean);
        this.customAutoCreateAnnotationResolver.onCreated(bean, beanClass);
    }

    @Override
    public void cleanUp() {
        this.autoCreationAnnotationResolver.cleanUp();
        this.beanRegistry.cleanUp();
    }
}
