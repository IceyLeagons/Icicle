package net.iceyleagons.icicle.core.configuration.environment;

import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.core.configuration.Configuration;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

public interface ConfigurationEnvironment {

    void addConfiguration(Configuration configuration);

    void updateValues();

    Optional<Object> getProperty(String path);

    <T> Optional<T> getProperty(String path, Class<T> type);

    Collection<Configuration> getConfigurations();

    Configuration getConfiguration(Class<?> declaringType);

    File getConfigRootFolder();

    @Internal
    void cleanUp();
}
