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

package net.iceyleagons.icicle.core;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.DefaultBeanManager;
import net.iceyleagons.icicle.core.beans.GlobalServiceProvider;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironmentImpl;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.proxy.ByteBuddyProxyHandler;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Base Icicle application. Most implementations of Icicle <b>SHOULD</b> extend from this.
 * <p>
 * Example usage:
 * <pre>{@code new AbstractIcicleApplication("net.iceyleagons.test.icicle.core.bean.resolvable", ExecutionUtils.debugHandler(), null) {
 *   @Override
 *   public String getName() {
 *     return "test";
 *   }
 * };}</pre>
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public abstract class AbstractIcicleApplication implements Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIcicleApplication.class);

    private final Reflections reflections;
    private final ExecutionHandler executionHandler;
    private final GlobalServiceProvider globalServiceProvider;

    private final BeanManager beanManager;
    private final ConfigurationEnvironment configurationEnvironment;

    /**
     * Instantiates a new instance of the base application.
     * This will call {@link #AbstractIcicleApplication(String, ExecutionHandler, GlobalServiceProvider, File, ClassLoader...)} with "configs" as the config directory, and will define no additional {@link ClassLoader}s.
     *
     * @param rootPackage the package in which all the icicle annotated classes are contained. <i>(Icicle searches through subclasses as well.)</i>
     * @param executionHandler an instance of a {@link net.iceyleagons.icicle.core.utils.ExecutionHandler} fully implemented.
     * @param globalServiceProvider an instance of a {@link net.iceyleagons.icicle.core.beans.GlobalServiceProvider}.
     */
    public AbstractIcicleApplication(String rootPackage, ExecutionHandler executionHandler, GlobalServiceProvider globalServiceProvider) {
        this(rootPackage, executionHandler, globalServiceProvider, new File("configs"));
    }

    /**
     * Instantiates a new instance of the base application
     *
     * @param rootPackage           the package in which all the icicle annotated classes are contained. <i>(Icicle searches through subclasses as well.)</i>
     * @param executionHandler      an instance of a {@link net.iceyleagons.icicle.core.utils.ExecutionHandler} fully implemented.
     * @param globalServiceProvider an instance of a {@link net.iceyleagons.icicle.core.beans.GlobalServiceProvider}.
     * @param configRoot            the root folder, where the configs will be placed
     * @param classLoaders the classLoader to use with {@link Reflections}
     */
    @SneakyThrows
    public AbstractIcicleApplication(String rootPackage, ExecutionHandler executionHandler, GlobalServiceProvider globalServiceProvider, File configRoot, ClassLoader... classLoaders) {
        PerformanceLog.begin(this, "Application Creation", AbstractIcicleApplication.class);
        this.reflections = new Reflections(
                new ConfigurationBuilder()
                        .setScanners(Scanners.values())
                        .setExpandSuperTypes(false)
                        .filterInputsBy(new FilterBuilder().includePackage(rootPackage).includePackage("net.iceyleagons.icicle"))
                        .setUrls(ClasspathHelper.forPackage(rootPackage, classLoaders))
        ).merge(Icicle.ICICLE_REFLECTIONS);

        this.beanManager = new DefaultBeanManager(this);
        this.executionHandler = executionHandler;
        this.globalServiceProvider = globalServiceProvider;

        this.configurationEnvironment = new ConfigurationEnvironmentImpl(new AdvancedFile(configRoot, true).asFile(), ByteBuddyProxyHandler.getNewByteBuddyInstance());

        this.beanManager.getBeanRegistry().registerBean(Application.class, this); //registering self instance
        this.beanManager.getBeanRegistry().registerBean(ConfigurationEnvironment.class, configurationEnvironment);
        this.beanManager.getBeanRegistry().registerBean(ExecutionHandler.class, getExecutionHandler());

        PerformanceLog.end(this);
    }

    protected void onConstructed() {}

    @Override
    public void start() throws Exception {
        LOGGER.info("Booting Icicle application named: " + getName());

        PerformanceLog.begin(this, "Application start", AbstractIcicleApplication.class);
        this.beanManager.scanAndCreateBeans();
        onConstructed();
        PerformanceLog.end(this);
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down Icicle application named: " + getName());
        this.beanManager.cleanUp();
        this.configurationEnvironment.cleanUp();
    }

    @Override
    public BeanManager getBeanManager() {
        return this.beanManager;
    }

    @Override
    public ConfigurationEnvironment getConfigurationEnvironment() {
        return this.configurationEnvironment;
    }

    @Override
    public Reflections getReflections() {
        return this.reflections;
    }

    @Override
    public ExecutionHandler getExecutionHandler() {
        return this.executionHandler;
    }

    @Override
    public GlobalServiceProvider getGlobalServiceProvider() {
        return this.globalServiceProvider;
    }
}
