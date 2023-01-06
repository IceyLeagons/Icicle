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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.CommandUtils;
import net.iceyleagons.icicle.commands.annotations.CommandParameter;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverRegistry;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class is used to hold data about command parameters.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@Getter
@RequiredArgsConstructor
public class ParameterInfo {

    /**
     * Name of the parameter
     */
    private final String name;

    /**
     * Description of the parameter
     */
    private final String description;

    /**
     * True type of the parameter (if it's an optional for ex. get the type inside the optional)
     */
    private final Class<?> type;

    /**
     * Annotations of the parameter
     */
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    /**
     * Whether the parameter is required (if it's not required it's encapsulated in a {@link java.util.Optional})
     */
    private final boolean required;

    /**
     * Options of the parameter. Used for auto completing in some contexts. Filled in automatically by the command system.
     */
    private final List<String> options;

    /**
     * Constructs a new ParameterInfo instance
     *
     * @param parameter the parameter
     * @param registry the parameter registry
     * @return the resulting instance
     * @throws IllegalStateException if no parameter resolvers are found for the parameter
     */
    public static ParameterInfo from(Parameter parameter, ParameterResolverRegistry registry) {
        if (parameter.isAnnotationPresent(CommandParameter.class)) {
            CommandParameter annotation = parameter.getAnnotation(CommandParameter.class);

            Class<?> type = CommandUtils.getActualParamType(parameter);
            if (type.equals(String.class)) {
                return new ParameterInfo(annotation.name(), annotation.description(), type, getAnnotations(parameter), CommandUtils.isRequired(parameter), Collections.emptyList());
            }

            ParameterResolverTemplate<?> resolver = type.isEnum() ? registry.get(Enum.class) : registry.get(type);
            if (resolver == null) {
                throw new IllegalStateException("No parameter resolver found for type " + type.getName() + " !");
            }

            return new ParameterInfo(annotation.name(), annotation.description(), type, getAnnotations(parameter), CommandUtils.isRequired(parameter), resolver.getOptions(type, parameter));
        }

        return null;
    }

    /**
     * Utility method to map the annotations of the parameter into a map, where the key is the annotation type, the value is the actual {@link Annotation} instance.
     *
     * @param parameter the parameter
     * @return the resulting {@link Map}
     */
    private static Map<Class<? extends Annotation>, Annotation> getAnnotations(Parameter parameter) {
        return Arrays.stream(parameter.getAnnotations()).collect(Collectors.toMap(Annotation::annotationType, v -> v));
    }
}
