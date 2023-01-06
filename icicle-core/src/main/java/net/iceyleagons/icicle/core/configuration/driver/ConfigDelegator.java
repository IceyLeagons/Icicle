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
import net.bytebuddy.implementation.attribute.AnnotationRetention;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.config.Config;
import net.iceyleagons.icicle.core.annotations.config.ConfigurationDriver;
import net.iceyleagons.icicle.core.configuration.Configuration;
import net.iceyleagons.icicle.core.exceptions.ConfigDelegationException;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Map;

/**
 * The ConfigDelegator is responsible for routing a config's (basically just classes, implementing {@link Configuration},
 * so all methods would throw {@link net.iceyleagons.icicle.utilities.exceptions.UnImplementedException}) methods to a driver (actual implementation)
 * based on their extension.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 10, 2022
 */
@RequiredArgsConstructor
public class ConfigDelegator {

    private final ByteBuddy bytebuddy;
    private final Path configRootFolder;
    private final Map<String, ConfigDriver> drivers = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    /**
     * Used for extracting the extension from {@link Config#value()}.
     * It's using the RegEx: <b>\\.</b>
     *
     * @param annotation the annotation
     * @return the extracted extension
     */
    private static String getExtension(Config annotation) {
        String[] array = annotation.value().split("\\.");
        return array[array.length - 1];
    }

    /**
     * Registers a new {@link ConfigDriver}.
     *
     * @param driver     the actual driver
     * @param annotation the {@link ConfigurationDriver} marking it
     */
    public void addDriver(ConfigDriver driver, ConfigurationDriver annotation) {
        String[] extensions = annotation.value();
        for (String extension : extensions) {
            drivers.put(extension, driver);
        }
    }

    /**
     * The actual method responsible for routing the default methods to an actual implementation.
     * It's using {@link ByteBuddy} passed from the constructor to do this.
     * The supplied configClass is taken as parent class, the implementation is a subclass from it.
     *
     * <b>Note that the passed constructor should not have any parameter types, as this method does not handle autowiring!</b>
     *
     * @param configClass the configClass
     * @param constructor the constructor to use (should be free of parameters)
     * @param annotation  the {@link Config} annotation of the class
     * @return the {@link Configuration} instance with implemented methods.
     * @throws ConfigDelegationException if the config cannot be delegated (/implemented)
     * @see ByteBuddy
     * @see ByteBuddy#subclass(Class)
     */
    public Configuration implementConfig(Class<?> configClass, Constructor<?> constructor, Config annotation) throws ConfigDelegationException {
        if (constructor.getParameterCount() != 0) {
            throw new ConfigDelegationException("Configurations must have one public empty constructors. (No parameters allowed)");
        }

        try {
            ConfigDriver driver = getDriver(annotation, configClass);
            Configuration config = (Configuration) bytebuddy.with(AnnotationRetention.ENABLED).subclass(configClass)
                    .method(ElementMatchers.isPublic()).intercept(MethodDelegation.to(driver))
                    .make()
                    .load(constructor.getDeclaringClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                    .getLoaded().getDeclaredConstructor(constructor.getParameterTypes())
                    .newInstance();

            config.setDeclaringType(configClass);
            config.setOrigin(config);
            config.afterConstruct(annotation, configRootFolder);
            return config;
        } catch (Exception e) {
            throw new ConfigDelegationException("Configuration delegation cannot be created due to exceptions.", e);
        }
    }

    /**
     * Returns a new instance of a {@link ConfigDriver} for the specified extension.
     * (P.S: Can be improved see comment in code)
     *
     * @param annotation the annotation to get the extension from
     * @param clazz      the configClass (only used inside the exception as an information)
     * @return the resulting {@link ConfigDriver}
     * @throws ConfigDelegationException if no driver found for extension
     */
    private ConfigDriver getDriver(Config annotation, Class<?> clazz) throws ConfigDelegationException {
        String extension = getExtension(annotation);

        if (!drivers.containsKey(extension)) {
            throw new ConfigDelegationException("No driver found for config extension: \"" + extension + "\". Used by: " + clazz.getName() + ". (Do you have the driver installed for it?)");
        }

        // TODO we have a not used ConfigDriver in registry we could mitigate,
        //  by not having it as an auto create type, rather just store the class and instantiate it on the fly as needed.
        //  This is a lazy solution IK, but it works hey!
        return drivers.get(extension).newInstance();
    }
}
