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

package net.iceyleagons.icicle.core.configuration.driver;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.exceptions.ConfigDelegationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 10, 2022
 */
@RequiredArgsConstructor
public class ConfigDelegator {

    private final ByteBuddy bytebuddy;
    private final Path configRootFolder;
    private final Map<String, ConfigDriver> drivers = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    public void addDriver(ConfigDriver driver, ConfigurationDriver annotation) {
        String[] extensions = annotation.value();
        for (String extension : extensions) {
            drivers.put(extension, driver);
        }
    }

    public Configuration implementConfig(Class<?> configClass, Constructor<?> constructor, Config annotation) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (constructor.getParameterCount() != 0) {
            throw new ConfigDelegationException("Configurations must have one public empty constructors. (No parameters allowed)");
        }

        ConfigDriver driver = getDriver(annotation, configClass);
        Configuration config = (Configuration) bytebuddy.subclass(configClass)
                .method(ElementMatchers.isPublic()).intercept(MethodDelegation.to(driver))
                .make()
                .load(constructor.getDeclaringClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                .getLoaded().getDeclaredConstructor(constructor.getParameterTypes())
                .newInstance();

        config.setOriginType(configClass);
        config.setOrigin(config);
        config.afterConstruct(annotation, configRootFolder);
        return config;
    }

    private ConfigDriver getDriver(Config annotation, Class<?> clazz) {
        String extension = getExtension(annotation);

        if (!drivers.containsKey(extension)) {
            throw new ConfigDelegationException("No driver found for config extension: \"" + extension + "\". Used by: " + clazz.getName() + ". (Do you have the driver installed for it?)");
        }

        return drivers.get(extension).newInstance();
    }

    private static String getExtension(Config annotation) {
        String[] array = annotation.value().split("\\.");
        return array[array.length - 1];
    }
}
