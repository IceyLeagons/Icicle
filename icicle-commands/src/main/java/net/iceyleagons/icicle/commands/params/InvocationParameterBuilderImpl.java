/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.params;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.CommandUtils;
import net.iceyleagons.icicle.commands.annotations.CommandSender;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverRegistry;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverTemplate;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.QualifierKey;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Optional;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@RequiredArgsConstructor
public class InvocationParameterBuilderImpl implements InvocationParameterBuilder {

    private final BeanRegistry beanRegistry;
    private final ParameterResolverRegistry parameterResolverRegistry;

    @Override
    public Object[] buildParameters(Method method, Object sender, Object[] commandInputs, Map<Class<?>, Object> externalParams) {
        final Parameter[] params = method.getParameters();
        final Object[] response = new Object[params.length];

        int j = 0; // commandInput counter
        for (int i = 0; i < params.length; i++) {
            final Parameter parameter = params[i];
            final Class<?> type = CommandUtils.getActualParamType(parameter);

            if (externalParams.containsKey(type)) {
                response[i] = externalParams.get(type);
                continue;
            }

            if (parameter.isAnnotationPresent(CommandSender.class)) {
                response[i] = sender;
                continue;
            }

            // Since String is frequently used we don't write a param resolver for it for speed, also it does not require parsing.
            if (type.equals(String.class)) {
                response[i] = commandInputs[j++].toString();
                continue;
            }

            ParameterResolverTemplate<?> resolver = parameterResolverRegistry.get(type);
            if (resolver == null) {
                response[i] = encaseWithOptionalIfNecessary(getParamFromBeanRegistry(parameter, type), parameter);
                continue;
            }

            final Object resolved = resolver.parse(parameter, type, commandInputs[j++]);
            response[i] = encaseWithOptionalIfNecessary(resolved, parameter);
        }


        return response;
    }

    private Object getParamFromBeanRegistry(Parameter parameter, Class<?> type) {
        final QualifierKey key = new QualifierKey(type, QualifierKey.getQualifier(parameter));
        if (beanRegistry.isRegistered(key)) {
            throw new IllegalStateException("Unresolvable parameter!");
        }

        return beanRegistry.getBeanNullable(key);
    }

    private static Object encaseWithOptionalIfNecessary(Object value, Parameter parameter) {
        return CommandUtils.isRequired(parameter) ? value : Optional.ofNullable(value);
    }
}
