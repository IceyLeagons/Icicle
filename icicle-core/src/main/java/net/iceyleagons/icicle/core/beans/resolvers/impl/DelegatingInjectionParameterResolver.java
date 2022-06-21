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

package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.InjectionParameterResolver;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.other.QualifierKey;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * Default implementation of {@link InjectionParameterResolver}.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @see InjectionParameterResolver
 * @since Aug. 23, 2021
 */
public class DelegatingInjectionParameterResolver implements InjectionParameterResolver {

    private final AutowiringAnnotationResolver autowiringAnnotationResolver;

    public DelegatingInjectionParameterResolver(AutowiringAnnotationResolver autowiringAnnotationResolver) {
        this.autowiringAnnotationResolver = autowiringAnnotationResolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] resolveConstructorParameters(Executable executable, BeanRegistry beanRegistry) throws UnsatisfiedDependencyException {
        Parameter[] parameters = executable.getParameters();
        Object[] params = new Object[parameters.length];

        if (parameters.length == 0) return params;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String qualifier = QualifierKey.getQualifier(parameter);
            Class<?> type = parameter.getType();

            Object result = beanRegistry.getBeanNullable(type, qualifier);

            if (parameter.getAnnotations().length != 0 && result == null) {
                for (Annotation annotation : parameter.getAnnotations()) {
                    result = autowiringAnnotationResolver.getValueForAnnotation(annotation.annotationType(), annotation, type);
                    if (result != null) break;
                }
            }

            if (result == null) throw new UnsatisfiedDependencyException(parameter);

            params[i] = result;
        }

        return params;
    }
}
