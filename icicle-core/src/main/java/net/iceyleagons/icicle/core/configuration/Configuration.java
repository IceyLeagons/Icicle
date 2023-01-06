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

package net.iceyleagons.icicle.core.configuration;

import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.utilities.exceptions.UnImplementedException;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import net.iceyleagons.icicle.utilities.lang.Internal;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

/**
 * Interface for any configuration type.
 * These are implemented via {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDriver}s managed via {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 26, 2021
 */
public interface Configuration {

    /**
     * Adds default values to configuration.
     * If the configuration does not have a property, the default value will be inserted, in any other case this does nothing.
     * <p>
     * Save must be called after this!
     *
     * @param path the config path
     * @param object the object to add as default
     */
    default void addDefault(String path, Object object) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Saves the configuration. Writes the in-memory values to the disk.
     */
    default void save() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Reloads the configuration. Discard the in-memory values and reads them back in from file.
     * WARNING! Not saved values will be erased.
     */
    default void reload() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Returns the value for the given path in this given configuration.
     * If the configuration does not have an entry for the path, the return will be null.
     *
     * @param path the configuration path
     * @return the value or null if not present
     */
    @Nullable
    default Object get(String path) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Returns all the in-memory values of the configuration.
     * Please note, that this is not necessarily a 100% representation of the configuration on the disk.
     *
     * @return all the entries of the in-memory cache
     */
    default Set<Map.Entry<String, Object>> getValues() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * @return the class (not proxied) which declares this configuration.
     */
    default Class<?> declaringType() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Sets the configuration's disk-based file to the supplied one.
     *
     * @param configFile the new file
     * @see AdvancedFile
     */
    @Internal
    default void setConfigFile(AdvancedFile configFile) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Sets the bean, which declares this configuration (the actual implementation).
     * Normally this is set via {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator}
     *
     * @param origin the new implementation
     * @see Internal
     */
    @Internal
    default void setOrigin(Object origin) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Sets the bean's original type (not proxied), which declares this configuration.
     * Normally this is set via {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator}
     *
     * @param declaringType the new declaring type
     * @see Internal
     */
    @Internal
    default void setDeclaringType(Class<?> declaringType) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Sets the header of the configuration.
     * Note, that not all configuration file types support this feature.
     *
     * @param header the new header
     */
    default void setHeader(String header) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    /**
     * Called by {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator} to pass necessary values to the implementation.
     * This is basically holds the functionality of a constructor.
     *
     * @param annotation the annotation on the declaring class
     * @param configRootFolder the root directory for configuration files
     * @see Internal
     */
    @Internal
    default void afterConstruct(Config annotation, Path configRootFolder) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }
}
