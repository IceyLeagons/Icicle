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
import net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

/**
 * ConfigurationEnvironment is used to access properties within all the scanned/created configuration files.
 * It is also the manager for all {@link Configuration} instances.
 * <br><br>
 * Additionally, this manages all configurations that are scanned from file system, rather than created by developers via code.
 * This includes the following files "application.properties", "application.yml", "config.properties", "config.yml" under the directory configs/ or resources/configs/
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 26, 2021
 */
public interface ConfigurationEnvironment {

    /**
     * Registers a new {@link Configuration} to the environment
     *
     * @param configuration the config to register
     */
    void addConfiguration(Configuration configuration);

    /**
     * Updates values inside the environment.
     * It calls every registered configs {@link Configuration#reload()} method, then collects all values and puts them
     * inside the central cache.
     */
    void updateValues();

    /**
     * Used to get a property by the given path.
     * This is taken from the central cache.
     *
     * @param path the property path
     * @return the Optional containing the value or empty if the path does not have associated values with it
     */
    Optional<Object> getProperty(String path);

    /**
     * Used to get a property by the given path, then casts property to the required type.
     * This is taken from the central cache.
     *
     * @param path the property path
     * @param type the property to be cast to
     * @return the Optional containing the value or empty if the path does not have associated values with it
     * @param <T> the wanted type
     */
    <T> Optional<T> getProperty(String path, Class<T> type);

    /**
     * @return all the registered {@link Configuration}s
     */
    Collection<Configuration> getConfigurations();

    /**
     * Used to get a specific configuration with it's declaring type.
     *
     * @param declaringType the type
     * @return the result config or null
     */
    @Nullable
    Configuration getConfiguration(Class<?> declaringType);

    /**
     * @return the root directory for configuration files
     */
    File getConfigRootFolder();

    /**
     * @return the {@link ConfigDelegator} used by the environment
     */
    ConfigDelegator getConfigDelegator();

    /**
     * Cleans up the configuration environment.
     * This should also call all registered configs {@link Configuration#save()} methods.
     */
    @Internal
    void cleanUp();
}
