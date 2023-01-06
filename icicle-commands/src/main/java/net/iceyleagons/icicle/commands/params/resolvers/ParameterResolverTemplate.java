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

package net.iceyleagons.icicle.commands.params.resolvers;

import net.iceyleagons.icicle.commands.params.ParamParsingException;
import net.iceyleagons.icicle.commands.params.ParameterInfo;

import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * ParameterResolvers are responsible for implementing parameter parsing for the command system.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 * @param <T> the output type of the resolver
 */
public interface ParameterResolverTemplate<T> {

    /**
     * The actual parsing function.
     *
     * @param parameter the parameter in question
     * @param type the true type of the parameter (<b>DO NOT USE {@link Parameter#getType()}, this accounts for Optionals etc.</b>)
     * @param value the used input value (mostly this will be a string)
     * @param info the parameter information
     * @param additionalParameters additional context parameters
     * @return the parsed object. <b>It must not be an optional</b>, the {@link net.iceyleagons.icicle.commands.params.InvocationParameterBuilder} will take care of that automatically.
     * @throws ParamParsingException if the parameter cannot be parsed
     */
    T parse(Parameter parameter, Class<?> type, Object value, ParameterInfo info, Map<Class<?>, Object> additionalParameters) throws ParamParsingException;

    /**
     * Returns the options for this parameter.
     * This is used in some contexts for auto completion.
     *
     * @param type the true type of the parameter (<b>DO NOT USE {@link Parameter#getType()}, this accounts for Optionals etc.</b>)
     * @param parameter the parameter in question
     * @return the list of options
     */
    default List<String> getOptions(Class<?> type, Parameter parameter) {
        return Collections.emptyList();
    }
}
