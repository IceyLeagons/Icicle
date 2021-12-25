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

package net.iceyleagons.icicle.core.configuration.environment;

import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


public class ConfigurationEnvironmentImpl implements ConfigurationEnvironment {

    private final Map<Class<?>, Configuration> configurations = new ConcurrentHashMap<>();
    private final Map<String, Object> values = new ConcurrentHashMap<>();
    private final File configRootFolder;

    public ConfigurationEnvironmentImpl(File configRootFolder) {
        this.configRootFolder = configRootFolder;
    }

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
        return value == null ? Optional.empty() : Optional.ofNullable(ReflectionUtils.castIfNecessary(type, value));
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
        return this.configRootFolder;
    }

    @Override
    public void cleanUp() {
        getConfigurations().forEach(Configuration::save);

        this.values.clear();
        this.configurations.clear();
    }
}
