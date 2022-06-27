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

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * This exception is used when a dependency can not be found when autowiring.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public class UnsatisfiedDependencyException extends Exception {

    /**
     * @param parameter the parameter of the constructor that caused the issue
     */
    public UnsatisfiedDependencyException(Parameter parameter) {
        super("Unsatisfied dependency expressed in " + parameter.getDeclaringExecutable().getDeclaringClass().getName() + " at parameter named " + parameter.getName() + ". Type: " + parameter.getType().getName() + " (Is it an @AutoCreate child?):");
    }

    /**
     * @param field the field of the class that caused the issue
     */
    public UnsatisfiedDependencyException(Field field) {
        super("Unsatisfied dependency expressed in " + field.getDeclaringClass().getName() + " at field named " + field.getName() + ". Type: " + field.getType().getName() + " (Is it an @AutoCreate child?):");
    }
}
