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

package net.iceyleagons.icicle.core.annotations.config;

import net.iceyleagons.icicle.core.annotations.bean.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * In an application ConfigurationDrivers are responsible for implementing different configuration types.
 * <p>
 * These drivers should all implement {@link net.iceyleagons.icicle.core.configuration.Configuration}.
 * The {@link Config}s method will be routed to these methods using the {@link net.iceyleagons.icicle.core.proxy.BeanProxyHandler}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @see Config
 * @see net.iceyleagons.icicle.core.configuration.Configuration
 * @see net.iceyleagons.icicle.core.proxy.BeanProxyHandler
 * @see AutoCreate
 * @since Jul. 19, 2022
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface ConfigurationDriver {

    /**
     * @return the extensions supported
     */
    String[] value();

}
