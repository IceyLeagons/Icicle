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

package net.iceyleagons.icicle.commands.params.resolvers.impl.numbers;

import net.iceyleagons.icicle.commands.CommandUtils;
import net.iceyleagons.icicle.commands.params.ParamParsingException;
import net.iceyleagons.icicle.commands.params.ParameterInfo;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverTemplate;

import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Abstraction layer for parsing numbers, as they are all similar but the actual parsing method is different.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 * @param <T> the output type of the resolver
 */
public abstract class AbstractNumberParser<T> implements ParameterResolverTemplate<T> {

    private final NumberValueMapper<T> mapper;
    private final String expected;

    /**
     * Creates a new number parser instance
     *
     * @param mapper the mapper function (ex.: {@link Integer#parseInt(String)}
     * @param expected the name of the type (used for the errors, ex. output: expected int)
     */
    public AbstractNumberParser(NumberValueMapper<T> mapper, String expected) {
        this.mapper = mapper;
        this.expected = expected;
    }

    @Override
    public T parse(Parameter parameter, Class<?> type, Object value, ParameterInfo info, Map<Class<?>, Object> additionalParameters) throws ParamParsingException {
        String arg = value.toString();

        try {
            return mapper.parse(arg);
        } catch (NumberFormatException e) {
            throw new ParamParsingException(CommandUtils.PARAMETER_PARSER_VALUE_ERROR_KEY, CommandUtils.PARAMETER_PARSER_VALUE_ERROR, Map.of("expected", expected));
        }
    }

    /**
     * Abstraction layer for Java built-in number parses implementations.
     *
     * @param <T> the output type
     */
    interface NumberValueMapper<T> {

        /**
         * Parses the given string input to the appropriate number type via Java's built-in parsers.
         *
         * @param input the user input
         * @return the resulting number instance
         * @throws NumberFormatException if the input is not a valid number
         */
        T parse(String input) throws NumberFormatException;
    }
}