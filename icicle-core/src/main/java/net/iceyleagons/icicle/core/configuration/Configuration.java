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

import java.io.File;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public interface Configuration {

    default void addDefault(String path, Object object) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void save() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void reload() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default Object get(String path) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default Set<Map.Entry<String, Object>> getValues() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default Class<?> declaringType() {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void setConfigFile(AdvancedFile configFile) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void setOrigin(Object origin) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void setOriginType(Class<?> originType) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    default void setHeader(String header) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }

    @Internal
    default void afterConstruct(Config annotation, Path configRootFolder) {
        throw new UnImplementedException("Interface cannot be used standalone, requires a driver via Icicle core.");
    }
}
