package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.DefaultBeanManager;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironmentImpl;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public abstract class AbstractIcicleApplication implements Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIcicleApplication.class);

    private final Reflections reflections;

    private final BeanManager beanManager;
    private final ConfigurationEnvironment configurationEnvironment;

    public AbstractIcicleApplication(String rootPackage) {
        PerformanceLog.begin(this, "Application Creation", AbstractIcicleApplication.class);
        this.reflections = new Reflections(rootPackage).merge(Icicle.ICICLE_REFLECTIONS);
        this.beanManager = new DefaultBeanManager(this);

        this.configurationEnvironment = new ConfigurationEnvironmentImpl(new AdvancedFile(new File("configs"), true).getFile()); //TODO once Bukkit API is present

        this.beanManager.getBeanRegistry().registerBean(Application.class, this); //registering self instance
        this.beanManager.getBeanRegistry().registerBean(ConfigurationEnvironment.class, configurationEnvironment);

        PerformanceLog.end(this);
    }

    @Override
    public void start() throws BeanCreationException, CircularDependencyException, UnsatisfiedDependencyException {
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
}
