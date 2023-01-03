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

package net.iceyleagons.icicle.commands;

import net.iceyleagons.icicle.utilities.lang.Utility;

import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.util.Optional;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
@Utility
public class CommandUtils {

    public static final String PARAMETER_PARSER_VALUE_ERROR_KEY = "icicle.commands.params.valueError";
    public static final String PARAMETER_PARSER_VALUE_ERROR = "Invalid value give for parameter. Expected: {expected}.";
    private CommandUtils() {
    }

    public static Class<?> getActualParamType(Parameter parameter) {
        return isRequired(parameter) ? parameter.getType() : getOptionalType(parameter);
    }

    public static boolean isRequired(Parameter parameter) {
        return !parameter.getType().equals(Optional.class);
    }

    public static Class<?> getOptionalType(Parameter optional) {
        return (Class<?>) ((ParameterizedType) optional.getParameterizedType()).getActualTypeArguments()[0];
    }
}
