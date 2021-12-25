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

package net.iceyleagons.icicle.core.exceptions;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

/**
 * This exception is used when a bean cannot be created due to other exceptions/errors.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public class BeanCreationException extends Exception {

    /**
     * @param constructor the constructor that was used when attempted to create the bean
     * @param msg         the error message
     */
    public BeanCreationException(Constructor<?> constructor, String msg) {
        this(constructor, msg, null);
    }

    /**
     * @param constructor the constructor that was used when attempted to create the bean
     * @param msg         the error message
     * @param cause       the underlying exception, that caused this exception
     */
    public BeanCreationException(Constructor<?> constructor, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + constructor.getDeclaringClass().getName() + ": " + msg, cause);
    }

    /**
     * @param clazz the clazz that was used when attempted to create the bean
     * @param msg   the error message
     */
    public BeanCreationException(Class<?> clazz, String msg) {
        this(clazz, msg, null);
    }

    /**
     * @param clazz the clazz that was used when attempted to create the bean
     * @param msg   the error message
     * @param cause the underlying exception, that caused this exception
     */
    public BeanCreationException(Class<?> clazz, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + clazz.getName() + ": " + msg, cause);
    }
}
