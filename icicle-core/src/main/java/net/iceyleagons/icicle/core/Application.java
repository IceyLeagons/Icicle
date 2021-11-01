package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import org.reflections.Reflections;

public interface Application {

    void start() throws Exception;

    void shutdown();

    BeanManager getBeanManager();

    ConfigurationEnvironment getConfigurationEnvironment();

    Reflections getReflections();

    ExecutionHandler getExecutionHandler();
}
