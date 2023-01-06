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

    /**
     * Key for the parameter parsing exceptions. Used by the translation system.
     */
    public static final String PARAMETER_PARSER_VALUE_ERROR_KEY = "icicle.commands.params.valueError";

    /**
     * Default value for the parameter parsing exceptions. Used by the translation system.
     */
    public static final String PARAMETER_PARSER_VALUE_ERROR = "Invalid value give for parameter. Expected: {expected}.";
    private CommandUtils() {}

    /**
     * Used for getting the actual type of the parameter, meaning if it's an Optional
     * this method will return the generic type inside the Optional via {@link #getOptionalType(Parameter)},
     * otherwise the {@link Parameter#getType()} will be returned instead.
     *
     * @param parameter the parameter
     * @return the actual type of the parameter
     */
    public static Class<?> getActualParamType(Parameter parameter) {
        return isRequired(parameter) ? parameter.getType() : getOptionalType(parameter);
    }

    /**
     * Checks whether the parameter is an {@link Optional} or not.
     * If it's an optional then it's not a requried parameter.
     *
     * @param parameter the parameter to check
     * @return true if the parameter is required (meaning it's not an {@link Optional} type), false otherwise
     */
    public static boolean isRequired(Parameter parameter) {
        return !parameter.getType().equals(Optional.class);
    }

    /**
     * Used to retrieve the generic type inside an {@link Optional}.
     * <p>
     * <b>NOTE</b>: the parameter is not checked against whether it's an {@link Optional}. The calling
     * method should do that check instead.
     *
     * @param optional the optional parameter
     * @return the generic type
     */
    public static Class<?> getOptionalType(Parameter optional) {
        return (Class<?>) ((ParameterizedType) optional.getParameterizedType()).getActualTypeArguments()[0];
    }
}
