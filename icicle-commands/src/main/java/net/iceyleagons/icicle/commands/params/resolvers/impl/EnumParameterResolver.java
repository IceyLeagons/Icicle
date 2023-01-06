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

package net.iceyleagons.icicle.commands.params.resolvers.impl;

import net.iceyleagons.icicle.commands.annotations.ParameterResolver;
import net.iceyleagons.icicle.commands.params.ParamParsingException;
import net.iceyleagons.icicle.commands.params.ParameterInfo;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverTemplate;

import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Icicle's default parameter resolver for enums.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@ParameterResolver({ Enum.class })
public class EnumParameterResolver implements ParameterResolverTemplate<Object> {

    @Override
    public Object parse(Parameter parameter, Class<?> type, Object value, ParameterInfo info, Map<Class<?>, Object> additionalParameters) throws ParamParsingException {
        return Enum.valueOf((Class<? extends Enum>) type, value.toString().toUpperCase());
    }


    @Override
    public List<String> getOptions(Class<?> type, Parameter parameter) {
        return Arrays.stream(type.getEnumConstants()).map(o -> {
            String val = o.toString();
            return val.substring(0, 1).toUpperCase() + val.substring(1);
        }).collect(Collectors.toList());
    }
}
