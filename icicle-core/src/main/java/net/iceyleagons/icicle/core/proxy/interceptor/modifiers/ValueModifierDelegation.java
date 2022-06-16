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

package net.iceyleagons.icicle.core.proxy.interceptor.modifiers;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.*;
import net.iceyleagons.icicle.core.modifiers.ValueModifier;
import net.iceyleagons.icicle.core.modifiers.ValueModifierAutoCreateHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 16, 2022
 */
@RequiredArgsConstructor
public class ValueModifierDelegation {

    private final ValueModifierAutoCreateHandler handler;

    @RuntimeType
    public Object run(@This Object parent, @SuperMethod Method superMethod, @Origin Method method, @AllArguments Object[] params) throws Exception {
        if (params.length == 0 || method.getParameters().length == 0) return superMethod.invoke(parent);

        final Object[] newParams = new Object[params.length];
        final Parameter[] parameters = method.getParameters();
        final Map<Class<? extends Annotation>, ValueModifier> modifiers = handler.getModifiers();

        x:
        for (int i = 0; i < method.getParameters().length; i++) {
            Parameter parameter = parameters[i];

            for (Annotation annotation : parameter.getAnnotations()) {
                if (modifiers.containsKey(annotation.annotationType())) {
                    newParams[i] = modifiers.get(annotation.annotationType()).modify(params[i], parameter);
                    continue x; // We can only support one modifier per value
                }
            }

            newParams[i] = params[i];
        }

        return superMethod.invoke(parent, newParams);
    }
}
