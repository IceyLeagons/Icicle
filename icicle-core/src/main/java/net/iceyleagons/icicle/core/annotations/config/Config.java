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

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Marks a class as a {@link Config}.
 * All config classes must implement the {@link net.iceyleagons.icicle.core.configuration.Configuration} interface.
 * <p>
 * Configs are the second things to be initialized in the life cycle of the application (after {@link ConfigurationDriver}s)
 *
 * @author TOTHTOMI
 * @version 1.5.0
 * @since Aug. 26, 2021
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface Config {

    /**
     * @return the path to the config yaml file, relative from the plugin datafolder
     */
    String value();

    /**
     * @return the header, in a format where every element of the array represents a new line.
     */
    String[] headerLines() default {};

    String headerPrefixFirst() default ""; // TODO document these, 'cause I have no idea atm, what do they do :sweat_smile:

    String headerCommentPrefix() default "";

    String headerCommentSuffix() default "";

    String headerSuffixLast() default "";

}
