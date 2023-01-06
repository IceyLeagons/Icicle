/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.configuration.driver.impl;

import lombok.Setter;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver;
import net.iceyleagons.icicle.core.configuration.driver.ConfigDriver;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.iceyleagons.icicle.utilities.file.FileUtils;
import net.iceyleagons.icicle.utilities.lang.Experimental;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Configuration driver for the properties file type.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 23, 2022
 */
@Experimental
@ConfigurationDriver({"properties"})
public class PropertiesConfigurationDriver extends ConfigDriver {

    private Properties properties;
    @Setter
    private String header = null;

    @Override
    @SneakyThrows
    public void afterConstruct(Config annotation, Path configRootFolder) {
        setConfigFile(new AdvancedFile(configRootFolder.resolve(annotation.value())));
        Asserts.isTrue(!configFile.isDirectory(), "Config file must not be a folder!");

        this.properties = new Properties();
        if (annotation.headerLines().length != 0) {
            setHeader(String.join("\n", annotation.headerLines()));
        }

        final Path file = super.configFile.getPath();
        if (!Files.exists(file)) {
            Files.createFile(file);
            if (this.header != null) {
                FileUtils.appendFile(configFile.getPath(), this.header.split("\n"));
            }
        }

        this.load();
        this.loadDefaultValues();
    }

    @Override
    public void addDefault(String path, Object object) {
        if (!properties.containsKey(path)) {
            properties.put(path, object);
        }
    }

    /**
     * Saves the default values to disk, then reloads the config.
     */
    private void loadDefaultValues() {
        Set<Field> fields = getFields();
        Set<Map.Entry<String, Object>> values = getValues(fields);

        values.forEach((entry) -> {
            String path = entry.getKey();
            Object value = entry.getValue();

            if (!properties.contains(path)) {
                properties.put(path, value);
            }
        });

        // TODO comments (needs custom implementation)

        save();
        reload();
    }

    @SneakyThrows
    private void load() {
        try (InputStream is = Files.newInputStream(super.configFile.getPath())) {
            try (InputStreamReader isr = new InputStreamReader(is)) {
                this.properties.load(isr);
            }
        }
    }

    @Override
    @SneakyThrows
    public void save() {
        try (OutputStream os = Files.newOutputStream(super.configFile.getPath())) {
            this.properties.store(os, null);
        }
    }

    @Override
    public void reload() {
        load();
        super.reloadValues();
    }

    @Override
    public Object get(String path) {
        return this.properties != null ? this.properties.get(path) : null;
    }

    @Override
    protected ConfigDriver newInstance() {
        return new PropertiesConfigurationDriver();
    }

    // We need to include these here (rather than in super class), because of byte buddy.
    @Override
    public void setConfigFile(AdvancedFile configFile) {
        super.setConfigFile(configFile);
    }

    @Override
    public void setOrigin(Object origin) {
        super.setOrigin(origin);
    }

    @Override
    public void setDeclaringType(Class<?> declaringType) {
        super.setDeclaringType(declaringType);
    }
}
