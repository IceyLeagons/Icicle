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

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.SneakyThrows;
import net.bytebuddy.ByteBuddy;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.file.FileUtils;
import org.simpleyaml.configuration.file.YamlFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Default implementation of {@link ConfigurationEnvironment}
 */
public class ConfigurationEnvironmentImpl implements ConfigurationEnvironment {

    private static final String[] possibleResources = new String[] { "application.properties", "application.yml", "config.properties", "config.yml" };

    private final Map<Class<?>, Configuration> configurations = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    private final Map<String, Object> values = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    private final Map<String, Object> fixValues = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    private final ConfigDelegator delegator;
    private final File configRootFolder;

    public ConfigurationEnvironmentImpl(File configRootFolder, ByteBuddy bytebuddy) {
        this.configRootFolder = configRootFolder;
        this.delegator = new ConfigDelegator(bytebuddy, configRootFolder.toPath());

        this.scanForDefaultConfigurations();
    }

    @SneakyThrows
    private void scanForDefaultConfigurations() {
        for (String resourceName : possibleResources) {
            URL url = this.getClass().getResource(resourceName);
            if (url != null) {
                Path p = Path.of(url.toURI());
                String extension = FileUtils.getExtension(p);
                if (extension.equals("properties")) {
                    loadDefaultProperties(p);
                } else if (extension.equals("yml")) {
                    loadDefaultYaml(p);
                }
            }
        }

        Path configPath = Path.of("./configs");
        if (Files.exists(configPath) && Files.isDirectory(configPath)) {
            try (Stream<Path> files = Files.list(configPath)) {
                files.forEach(file -> {
                    try {
                        if (Files.exists(file) && !Files.isDirectory(file)) {
                            String extension = FileUtils.getExtension(file);
                            if (extension.equals("yml")) {
                                loadDefaultYaml(file);
                            } else if (extension.equals("properties")) {
                                loadDefaultProperties(file);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    private void loadDefaultYaml(Path path) throws Exception {
        try (InputStream is = Files.newInputStream(path)) {
            YamlFile yamlFile = new YamlFile();
            yamlFile.load(is);

            this.fixValues.putAll(yamlFile.getMapValues(true));
        }
    }

    private void loadDefaultProperties(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            Properties properties = new Properties();
            properties.load(is);

            properties.forEach((key, value) -> this.fixValues.put(key.toString(), value));
        }
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
        if (fixValues.containsKey(path)) {
            return Optional.ofNullable(fixValues.get(path));
        }

        return Optional.ofNullable(values.get(path));
    }

    @Override
    public <T> Optional<T> getProperty(String path, Class<T> type) {
        if (fixValues.containsKey(path)) {
            return Optional.ofNullable(ReflectionUtils.castIfNecessary(type, fixValues.get(path)));
        }

        return Optional.ofNullable(ReflectionUtils.castIfNecessary(type, values.get(path)));
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
    public ConfigDelegator getConfigDelegator() {
        return this.delegator;
    }

    @Override
    public void cleanUp() {
        getConfigurations().forEach(Configuration::save);

        this.values.clear();
        this.configurations.clear();
    }
}
