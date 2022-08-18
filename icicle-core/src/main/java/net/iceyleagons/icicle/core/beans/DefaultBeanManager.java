/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.core.beans;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.Bean;
import net.iceyleagons.icicle.core.annotations.MergedAnnotationResolver;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.proxy.MethodAdviceHandler;
import net.iceyleagons.icicle.core.annotations.handlers.proxy.MethodInterceptionHandler;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.CustomAutoCreateAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.DependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.InjectionParameterResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingAutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingCustomAutoCreateAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingDependencyTreeResolver;
import net.iceyleagons.icicle.core.beans.resolvers.impl.DelegatingInjectionParameterResolver;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.MultipleInstanceException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.proxy.BeanProxyHandler;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
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
    private final InjectionParameterResolver constructorParameterResolver;
    private final Reflections reflections;

    private final BeanProxyHandler beanProxyHandler;

    private final MergedAnnotationResolver autoCreationAnnotationResolver;

    private final AutowiringAnnotationResolver autowiringAnnotationResolver;
    private final CustomAutoCreateAnnotationResolver customAutoCreateAnnotationResolver;

    private final Application application;

    public DefaultBeanManager(Application application) throws MultipleInstanceException {
        this.application = application;
        this.reflections = application.getReflections();

        this.beanRegistry = new DelegatingBeanRegistry(application);
        this.beanRegistry.registerBean(BeanRegistry.class, beanRegistry);
        this.beanRegistry.registerBean(DelegatingBeanRegistry.class, beanRegistry);

        this.autowiringAnnotationResolver = new DelegatingAutowiringAnnotationResolver();
        this.customAutoCreateAnnotationResolver = new DelegatingCustomAutoCreateAnnotationResolver();

        PerformanceLog.begin(application, "AutoCreate Ann. Res. Creation & Scanning", DefaultBeanManager.class);
        this.autoCreationAnnotationResolver = new MergedAnnotationResolver(AutoCreate.class, reflections);
        PerformanceLog.end(application);

        this.dependencyTreeResolver = new DelegatingDependencyTreeResolver(this.beanRegistry, this.autowiringAnnotationResolver, this.autoCreationAnnotationResolver);

        this.beanProxyHandler = new ByteBuddyProxyHandler();
        this.constructorParameterResolver = new DelegatingInjectionParameterResolver(autowiringAnnotationResolver);

    }

    /**
     * Helper method to retrieve and remove a specific sub-group of @{@link AutoCreate} from the "collective set" containing all of them
     * from {@link MergedAnnotationResolver}.
     *
     * <b>WARNING!</b> To save memory all the sub-groups retrieved using the method will be deleted from the autoCreationTypes collection.
     *
     * @param annotation        the annotation sub-group
     * @param autoCreationTypes the "collective set" of all auto-create types.
     * @return the resulting Set
     */
    private static Set<Class<?>> getAndRemoveTypesAnnotatedWith(Class<? extends Annotation> annotation, Set<Class<?>> autoCreationTypes) {
        Iterator<Class<?>> iterator = autoCreationTypes.iterator(); //creating an iterator to avoid concurrent modification
        Set<Class<?>> result = new ObjectOpenHashSet<>();


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
     * Creates the config drivers.
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws Exception if anything goes wrong.
     * @see ConfigurationDriver
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see Configuration
     * @see net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     */
    private void createConfigDrivers(Set<Class<?>> autoCreationTypes) throws Exception {
        PerformanceLog.begin(application, "Creating config drivers", DefaultBeanManager.class);

        final ConfigDelegator delegator = this.application.getConfigurationEnvironment().getConfigDelegator();
        Set<Class<?>> drivers = getAndRemoveTypesAnnotatedWith(ConfigurationDriver.class, autoCreationTypes);

        for (Class<?> driver : drivers) {
            if (!driver.isAnnotationPresent(ConfigurationDriver.class)) {
                throw new IllegalStateException("Driver is not annotated with @ConfigurationDriver!");
            }

            createAndRegisterBean(driver);

            Object driverObject = this.beanRegistry.getBeanNullable(driver);
            if (!(driverObject instanceof net.iceyleagons.icicle.core.configuration.driver.ConfigDriver)) {
                LOGGER.warn("ConfigDriver described by {} does not extend any ConfigDriver.", driver.getName());
                this.beanRegistry.unregisterBean(driver);
                continue;
            }

            delegator.addDriver((net.iceyleagons.icicle.core.configuration.driver.ConfigDriver) driverObject, driver.getAnnotation(ConfigurationDriver.class));
        }

        PerformanceLog.end(application);
    }

    /**
     * Creates and registers all the {@link Config}s.
     * All the configs created are casted to {@link Configuration} and registered into the {@link net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment}
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see Config
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see Configuration
     * @see net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     */
    private void createConfigs(Set<Class<?>> autoCreationTypes) throws Exception {

        PerformanceLog.begin(application, "Creating configs", DefaultBeanManager.class);
        Set<Class<?>> configs = getAndRemoveTypesAnnotatedWith(Config.class, autoCreationTypes);

        for (Class<?> config : configs) {
            Config annotation = config.getAnnotation(Config.class);
            PerformanceLog.begin(application, "Creating config: " + annotation.value(), DefaultBeanManager.class);

            Configuration configuration = this.application.getConfigurationEnvironment().getConfigDelegator().implementConfig(config, BeanUtils.getResolvableConstructor(config), annotation);

            this.beanRegistry.registerBean(config, configuration);
            this.application.getConfigurationEnvironment().addConfiguration(configuration);
            PerformanceLog.end(application);
        }

        this.application.getConfigurationEnvironment().updateValues();

        PerformanceLog.end(application);
    }

    /**
     * Creates and registers all the {@link MethodAdviceHandler}s.
     * These are used by an implementation of {@link BeanProxyHandler}. ({@link ByteBuddyProxyHandler} by default)
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see MethodAdviceHandler
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     * @see BeanProxyHandler
     */
    private void createAndRegisterMethodInterceptorsAndAdvices(Set<Class<?>> autoCreationTypes) throws Exception {
        PerformanceLog.begin(application, "Creating method interceptors", DefaultBeanManager.class);

        Set<Class<?>> advices = getAndRemoveTypesAnnotatedWith(MethodAdviceHandler.class, autoCreationTypes);
        Set<Class<?>> interceptors = getAndRemoveTypesAnnotatedWith(MethodInterceptionHandler.class, autoCreationTypes);

        for (Class<?> advice : advices) {
            createAndRegisterBean(advice);
            Object object = this.beanRegistry.getBeanNullable(advice);

            if (object instanceof MethodAdviceHandlerTemplate) {
                this.beanProxyHandler.registerAdviceTemplate((MethodAdviceHandlerTemplate) object);
            }
        }

        for (Class<?> interceptor : interceptors) {
            createAndRegisterBean(interceptor);
            Object obj = this.beanRegistry.getBeanNullable(interceptor);

            if (obj instanceof MethodInterceptorHandlerTemplate) {
                this.beanProxyHandler.registerInterceptorTemplate((MethodInterceptorHandlerTemplate) obj);
            }
        }

        PerformanceLog.end(application);
    }

    /**
     * Creates and registers all the {@link AnnotationHandler}s.
     *
     * @param autoCreationTypes the set of all the {@link AutoCreate} annotated types from {@link MergedAnnotationResolver}
     *                          (this method calls {@link #getAndRemoveTypesAnnotatedWith(Class, Set)} with this parameter)
     * @throws BeanCreationException          if any other exception prevents the creation of a bean
     * @throws CircularDependencyException    if one of the beans' dependencies form a circle
     * @throws UnsatisfiedDependencyException if a bean cannot be created due to missing dependencies
     * @see AnnotationHandler
     * @see AutoCreate
     * @see MergedAnnotationResolver
     * @see #getAndRemoveTypesAnnotatedWith(Class, Set)
     */
    private void createAnnotationHandlers(Set<Class<?>> autoCreationTypes) throws Exception {
        PerformanceLog.begin(application, "Creating annotation handlers", DefaultBeanManager.class);
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

        PerformanceLog.end(application);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void scanAndCreateBeans() throws Exception {
        PerformanceLog.begin(application, "Bean scanning & creation", DefaultBeanManager.class);

        PerformanceLog.begin(application, "Retrieving AutoCreate types", DefaultBeanManager.class);
        Set<Class<?>> autoCreationTypes = this.autoCreationAnnotationResolver.getAllTypesAnnotated();
        PerformanceLog.end(application);

        // The order down below is important! DO NOT CHANGE ORDER OF CALL!
        // First we want to create all the configurations because other beans may need them during construction
        createConfigDrivers(autoCreationTypes);
        createConfigs(autoCreationTypes);

        // Second we want to register all autowiring annotation handlers before creating beans
        createAnnotationHandlers(autoCreationTypes);

        createAndRegisterMethodInterceptorsAndAdvices(autoCreationTypes);

        PerformanceLog.begin(application, "Creating non-exclusive beans", DefaultBeanManager.class);
        for (Class<?> autoCreationType : autoCreationTypes) {
            createAndRegisterBean(autoCreationType);
        }
        PerformanceLog.end(application);

        PerformanceLog.end(application);
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
    public InjectionParameterResolver getConstructorParameterResolver() {
        return this.constructorParameterResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Reflections getReflectionsInstance() {
        return this.reflections;
    }

    @Override
    public BeanProxyHandler getProxyHandler() {
        return this.beanProxyHandler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createAndRegisterBean(Class<?> beanClass) throws Exception {
        if (beanClass == String.class || beanClass.isPrimitive()) return;

        if (!this.beanRegistry.isRegistered(beanClass)) { //this is here because a class may have multiple auto-create annotations and also just a precaution
            LOGGER.debug("Creating and registering bean of type: {}", beanClass.getName());

            Constructor<?> constructor = BeanUtils.getResolvableConstructor(beanClass);

            if (constructor.getParameterTypes().length == 0) {
                Object bean = BeanUtils.instantiateClass(constructor, this.beanProxyHandler);

                autowireSetters(beanClass, bean);
                BeanUtils.invokePostConstructor(beanClass, bean);
                registerBean(beanClass, bean);
                callBeanMethodsInsideBean(beanClass, bean);
                return;
            }

            resolveDependencyTree(beanClass);

            Object[] parameters = this.constructorParameterResolver.resolveConstructorParameters(constructor, getBeanRegistry());
            Object bean = BeanUtils.instantiateClass(constructor, this.beanProxyHandler, parameters);

            callBeanMethodsInsideBean(beanClass, bean);
            autowireSetters(beanClass, bean);
            BeanUtils.invokePostConstructor(beanClass, bean);
            registerBean(beanClass, bean);
        }
    }

    /**
     * Resolves the dependency tree for the given bean (for its constructor)
     *
     * @param beanClass the bean
     * @throws Exception if anything goes wrong
     */
    private void resolveDependencyTree(Class<?> beanClass) throws Exception {
        LinkedList<Class<?>> dependencies = this.dependencyTreeResolver.resolveDependencyTree(beanClass);

        LOGGER.debug("Found {} dependencies for bean of type {}", dependencies.size(), beanClass.getName());
        for (Class<?> dependency : dependencies) {
            createAndRegisterBean(dependency);
        }
    }

    /**
     * Resolves the dependency tree for the given method
     *
     * @param method the method
     * @throws Exception if anything goes wrong
     */
    private void resolveDependencyTree(Method method) throws Exception {
        LinkedList<Class<?>> dependencies = this.dependencyTreeResolver.resolveDependencyTree(method);

        LOGGER.debug("Found {} dependencies for setter {}", dependencies.size(), method.getName());
        for (Class<?> dependency : dependencies) {
            createAndRegisterBean(dependency);
        }
    }

    /**
     * Can be called to auto-wire non-icicle beans.
     *
     * @param bean the bean to autowire
     */
    @SneakyThrows
    public void autowireSetters(Object bean) {
        autowireSetters(bean.getClass(), bean);
    }

    public void autowireSetters(Class<?> beanClass, Object bean) throws Exception {
        for (Method declaredMethod : beanClass.getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Autowired.class)) continue;

            resolveDependencyTree(declaredMethod);
            Object[] parameters = this.constructorParameterResolver.resolveConstructorParameters(declaredMethod, getBeanRegistry());

            declaredMethod.setAccessible(true);
            declaredMethod.invoke(bean, parameters);
        }
    }

    /**
     * Calls the @Bean marked methods inside the bean.
     * This is used first time to register and proxy all these methods.
     *
     * @param beanClass the beanClass
     * @param bean      the beanObject
     */
    private void callBeanMethodsInsideBean(Class<?> beanClass, Object bean) {
        for (Method method : Arrays.stream(beanClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Bean.class)).toList()) {
            try {
                method.setAccessible(true);
                method.invoke(bean); // BeanDelegation (in proxy) will take care of registration, we just need to invoke it once, to register it
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new IllegalStateException("Could not invoke @Bean method inside bean: " + beanClass.getName(), e);
            }
        }
    }

    /**
     * Helper method to call register the bean into the beanRegistry & also calling customAutoCreateAnnotationResolver.
     *
     * @param beanClass the type to register the bean as
     * @param bean      the bean
     * @see BeanRegistry#registerBean(Class, Object)
     * @see CustomAutoCreateAnnotationResolver#onCreated(Object, Class)
     */
    private void registerBean(Class<?> beanClass, Object bean) throws Exception {
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