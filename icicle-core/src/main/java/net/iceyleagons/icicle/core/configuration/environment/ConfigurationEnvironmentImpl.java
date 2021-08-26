package net.iceyleagons.icicle.core.configuration.environment;

import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.utils.BeanUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import java.util.concurrent.ConcurrentHashMap;


public class ConfigurationEnvironmentImpl implements ConfigurationEnvironment {

    private final Map<Class<?>, Configuration> configurations = new ConcurrentHashMap<>();
    private final Map<String, Object> values = new ConcurrentHashMap<>();


    @Override
    public void addConfiguration(Configuration configuration) {
        this.configurations.put(configuration.declaringType(), configuration);
    }

    @Override
    public void updateValues() {
        values.clear();

        getConfigurations().forEach(configuration ->
                configuration.getValues().forEach(entry ->
                        values.put(entry.getKey(), entry.getValue())));
    }


    @Override
    public Optional<Object> getProperty(String path) {
        return Optional.ofNullable(values.get(path));
    }

    @Override
    public <T> Optional<T> getProperty(String path, Class<T> type) {
        Object value = values.get(path);
        return value == null ? Optional.empty() : Optional.ofNullable(BeanUtils.castIfNecessary(type, value));
    }

    @Override
    public Collection<Configuration> getConfigurations() {
        return this.configurations.values();
    }

    @Override
    public Configuration getConfiguration(Class<?> declaringType) {
        return this.configurations.get(declaringType);
    }

    @Override
    public File getConfigRootFolder() {
        return null;
    }
}
