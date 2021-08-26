package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import org.reflections.Reflections;

public interface Application {

    BeanManager getBeanManager();
    ConfigurationEnvironment getConfigurationEnvironment();
    Reflections getReflections();

}
