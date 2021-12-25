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

import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.DefaultBeanManager;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironmentImpl;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AbstractIcicleApplication implements Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIcicleApplication.class);

    private final Reflections reflections;
    private final ExecutionHandler executionHandler;

    private final BeanManager beanManager;
    private final ConfigurationEnvironment configurationEnvironment;

    public AbstractIcicleApplication(String rootPackage, ExecutionHandler executionHandler) {
        PerformanceLog.begin(this, "Application Creation", AbstractIcicleApplication.class);
        this.reflections = new Reflections(rootPackage).merge(Icicle.ICICLE_REFLECTIONS);
        this.beanManager = new DefaultBeanManager(this);
        this.executionHandler = executionHandler;

        this.configurationEnvironment = new ConfigurationEnvironmentImpl(new AdvancedFile(new File("configs"), true).getFile()); //TODO once Bukkit API is present

        this.beanManager.getBeanRegistry().registerBean(Application.class, this); //registering self instance
        this.beanManager.getBeanRegistry().registerBean(ConfigurationEnvironment.class, configurationEnvironment);
        this.beanManager.getBeanRegistry().registerBean(ExecutionHandler.class, getExecutionHandler());

        PerformanceLog.end(this);
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Booting Icicle application named: TODO");

        PerformanceLog.begin(this, "Application start", AbstractIcicleApplication.class);
        this.beanManager.scanAndCreateBeans();
        PerformanceLog.end(this);
    }

    @Override
    public void shutdown() {
        LOGGER.info("Shutting down Icicle application named: TODO");
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
}
