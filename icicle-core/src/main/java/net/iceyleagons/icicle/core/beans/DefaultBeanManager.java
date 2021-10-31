package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.MethodAdviceHandler;
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
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.performance.ExecutionLog;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
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

/**
 * Default implementation of {@link BeanManager}.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
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

        ExecutionLog.begin(application, "AutoCreate Ann. Res. Creation & Scanning", DefaultBeanManager.class);
        this.autoCreationAnnotationResolver = new MergedAnnotationResolver(AutoCreate.class, reflections);
        ExecutionLog.end(application);
    }

    /**
     * Helper method to retrieve and remove a specific sub-group of @{@link AutoCreate} from the "collective set" containing all of them
     * from {@link MergedAnnotationResolver}.
     *
     * <b>WARNING!</b> To save memory all the sub-groups retrieved using the method will be deleted from the autoCreationTypes collection.
     *
     * @param annotation the annotation sub-group
     * @param autoCreationTypes the "collective set" of all auto-create types.
     * @return the resulting Set
     */
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

    /**
     * Creates and registers all the {@link Config}s.
     * All the configs created are casted to {@link Configuration} and registered into the {@link net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment}
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException if any other exception prevents the creation of a bean
     * @throws CircularDependencyException if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see Config
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see Configuration
     * @see net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     */
    private void createConfigs(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
        ExecutionLog.begin(application, "Creating configs", DefaultBeanManager.class);
        Set<Class<?>> configs = getAndRemoveTypesAnnotatedWith(Config.class, autoCreationTypes);

        for (Class<?> config : configs) {
            Config annotation = config.getAnnotation(Config.class);
            ExecutionLog.begin(application, "Creating config: " + annotation.value(), DefaultBeanManager.class);

            createAndRegisterBean(config);
            Object object = this.beanRegistry.getBeanNullable(config);

            if (!(object instanceof Configuration)) {
                LOGGER.warn("Config described by {} does not extend any Configuration instance. (Did you forget to extend AbstractConfiguration?)", config.getName());
                this.beanRegistry.unregisterBean(config);
                continue;
            }

            Configuration configuration = (Configuration) object;


            configuration.setConfigFile(new AdvancedFile(new File(this.application.getConfigurationEnvironment().getConfigRootFolder(), annotation.value())));

            configuration.setOrigin(object);
            configuration.setOriginType(config);

            if (annotation.headerLines().length != 0) {
                configuration.setHeader(String.join("\n", annotation.headerLines()));
            }

            configuration.afterConstruct();
            this.application.getConfigurationEnvironment().addConfiguration(configuration);
            ExecutionLog.end(application);
        }

        this.application.getConfigurationEnvironment().updateValues();

        ExecutionLog.end(application);
    }

    /**
     * Creates and registers all the {@link MethodAdviceHandler}s.
     * These are used by an implementation of {@link BeanProxyHandler}. ({@link ByteBuddyProxyHandler} by default)
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException if any other exception prevents the creation of a bean
     * @throws CircularDependencyException if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see MethodAdviceHandler
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     * @see BeanProxyHandler
     */
    private void createAndRegisterMethodInterceptors(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
        ExecutionLog.begin(application, "Creating method interceptors", DefaultBeanManager.class);
        Set<Class<?>> interceptors = getAndRemoveTypesAnnotatedWith(MethodAdviceHandler.class, autoCreationTypes);

        for (Class<?> interceptor : interceptors) {
            createAndRegisterBean(interceptor);
            Object object = this.beanRegistry.getBeanNullable(interceptor);

            if (object instanceof MethodAdviceHandlerTemplate) {
                this.beanProxyHandler.registerInterceptor((MethodAdviceHandlerTemplate) object);
            }
        }

        ExecutionLog.end(application);
    }

    /**
     * Creates and registers all the {@link AnnotationHandler}s.
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException if any other exception prevents the creation of a bean
     * @throws CircularDependencyException if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see AnnotationHandler
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set) 
     */
    private void createAnnotationHandlers(Set<Class<?>> autoCreationTypes) throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
        ExecutionLog.begin(application, "Creating annotation handlers", DefaultBeanManager.class);
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

        ExecutionLog.end(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scanAndCreateBeans() throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
        ExecutionLog.begin(application, "Bean scanning & creation", DefaultBeanManager.class);

        ExecutionLog.begin(application, "Retrieving AutoCreate types", DefaultBeanManager.class);
        Set<Class<?>> autoCreationTypes = this.autoCreationAnnotationResolver.getAllTypesAnnotated();
        ExecutionLog.end(application);

        // The order down below is important! DO NOT CHANGE ORDER OF CALL!
        // First we want to create all the configurations because other beans may need them during construction
        createConfigs(autoCreationTypes);

        // Second we want to register all autowiring annotation handlers before creating beans
        createAnnotationHandlers(autoCreationTypes);

        createAndRegisterMethodInterceptors(autoCreationTypes);

        ExecutionLog.begin(application, "Creating non-exclusive beans", DefaultBeanManager.class);
        for (Class<?> autoCreationType : autoCreationTypes) {
            createAndRegisterBean(autoCreationType);
        }
        ExecutionLog.end(application);

        ExecutionLog.end(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BeanRegistry getBeanRegistry() {
        return this.beanRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DependencyTreeResolver getDependencyTreeResolver() {
        return this.dependencyTreeResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConstructorParameterResolver getConstructorParameterResolver() {
        return this.constructorParameterResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reflections getReflectionsInstance() {
        return this.reflections;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndRegisterBean(Class<?> beanClass) throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
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

    /**
     * Helper method to call register the bean into the beanRegistry & also calling customAutoCreateAnnotationResolver.
     *
     * @param beanClass the type to register the bean as
     * @param bean the bean
     * @see BeanRegistry#registerBean(Class, Object) 
     * @see CustomAutoCreateAnnotationResolver#onCreated(Object, Class)
     */
    private void registerBean(Class<?> beanClass, Object bean) {
        this.beanRegistry.registerBean(beanClass, bean);
        this.customAutoCreateAnnotationResolver.onCreated(bean, beanClass);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUp() {
        this.autoCreationAnnotationResolver.cleanUp();
        this.beanRegistry.cleanUp();
    }
}
