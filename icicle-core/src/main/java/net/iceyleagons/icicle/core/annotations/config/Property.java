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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation can be used to autowire config properties into parameters.
 * This is handled via {@link net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment} and {@link net.iceyleagons.icicle.core.configuration.ConfigPropertyAutowiringHandler}.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Aug. 27, 2021
 *
 * @see net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment
 * @see net.iceyleagons.icicle.core.configuration.ConfigPropertyAutowiringHandler
 * @see net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler
 * @see net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler
 */
@Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface Property {

    /**
     * @return the config path
     */
    String value();

}
