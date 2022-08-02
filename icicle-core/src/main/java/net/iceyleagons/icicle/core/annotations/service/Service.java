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

package net.iceyleagons.icicle.core.annotations.service;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * In an application services are responsible for handling the actual logic of the application.
 *
 * The services can only be applied to classes, to mark them as a provider. From then, all the interfaces that are implemented by that class can be autowired
 * standalone, and the auto-wiring logic will inject that implementation.
 *
 * Please note, that an interface can only have one implementation (provider). To have multiple ones, use this annotation in conjunction with @{@link net.iceyleagons.icicle.core.annotations.Qualifier}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 25, 2021
 * @see net.iceyleagons.icicle.core.annotations.Qualifier
 * @see AutoCreate
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface Service {

}
