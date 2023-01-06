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

package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.beans.BeanManager;
import net.iceyleagons.icicle.core.beans.GlobalServiceProvider;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import org.reflections.Reflections;

/**
 * Root of an Icicle-using project. Gives access to all the internals of the Icicle framework.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public interface Application {

    /**
     * Starts this application instance.
     *
     * @throws Exception if an exception occurs during the initialization of the application.
     */
    void start() throws Exception;

    /**
     * Shuts down this application instance.
     */
    void shutdown();

    /**
     * @return the service provider which was provided to this application.
     * @see GlobalServiceProvider
     */
    GlobalServiceProvider getGlobalServiceProvider();

    /**
     * @return the bean manager used by this application.
     * @see BeanManager
     */
    BeanManager getBeanManager();

    /**
     * @return the environment in which this application was set up.
     * @see ConfigurationEnvironment
     */
    ConfigurationEnvironment getConfigurationEnvironment();

    /**
     * @return the instance of {@link org.reflections.Reflections} used by this application.
     * @see Reflections
     */
    Reflections getReflections();

    /**
     * @return the execution handler used by this application.
     * @see ExecutionHandler
     */
    ExecutionHandler getExecutionHandler();

    /**
     * @return the name of the application
     */
    String getName();
}
