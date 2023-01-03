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

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@Getter
@RequiredArgsConstructor
public class ParameterInfo {

    private final String name;
    private final String description;
    private final Class<?> type;
    private final Map<Class<? extends Annotation>, Annotation> annotations;
    private final boolean required;

    public static ParameterInfo from(Parameter parameter) {
        if (!parameter.isAnnotationPresent(CommandParameter.class)) {
            throw new IllegalArgumentException("Command parameters must be annotated with @CommandParameter!");
        }

        CommandParameter annotation = parameter.getAnnotation(CommandParameter.class);
        return new ParameterInfo(annotation.name(), annotation.description(), CommandUtils.getActualParamType(parameter), getAnnotations(parameter), CommandUtils.isRequired(parameter));
    }

    private static Map<Class<? extends Annotation>, Annotation> getAnnotations(Parameter parameter) {
        return Arrays.stream(parameter.getAnnotations()).collect(Collectors.toMap(Annotation::annotationType, v -> v));
    }
}
