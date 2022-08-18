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

package net.iceyleagons.icicle.core.beans;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.annotations.bean.Qualifier;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 06, 2022
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public final class QualifierKey {
    public static final String DEFAULT_NAME = "NON_SPECIFIED";

    private final Class<?> clazz;
    private final String name;

    public static String getQualifier(Parameter param) {
        if (param.isAnnotationPresent(Qualifier.class)) {
            return getIfEmptyUseDefault(param.getAnnotation(Qualifier.class).value());
        }

        return DEFAULT_NAME;
    }

    public static String getQualifier(Class<?> beanClass) {
        if (beanClass.isAnnotationPresent(Qualifier.class)) {
            return getIfEmptyUseDefault(beanClass.getAnnotation(Qualifier.class).value());
        }

        return DEFAULT_NAME;
    }

    public static String getQualifier(Method method) {
        if (method.isAnnotationPresent(Qualifier.class)) {
            return getIfEmptyUseDefault(method.getAnnotation(Qualifier.class).value());
        }

        return DEFAULT_NAME;
    }

    private static String getIfEmptyUseDefault(String value) {
        return value.isEmpty() ? DEFAULT_NAME : value;
    }
}

