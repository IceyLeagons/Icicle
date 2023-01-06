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

import net.iceyleagons.icicle.commands.RegisteredCommand;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Instances of this interface are tasked with building out the required arguments for command methods.
 * Default implementation: {@link InvocationParameterBuilderImpl}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
public interface InvocationParameterBuilder {

    /**
     * This method constructs the required arguments for executing a command parameter.
     *
     * @param method the target method
     * @param sender the command sender
     * @param cmd the command registered to that method
     * @param commandInputs the user entered inputs (in the order of the parameters, due to optionals this may not equal the length of the method parameters)
     * @param externalParams any additional context parameters (to be injected if requried)
     * @return the arguments in the order of the parameters
     * @throws ParamParsingException if the parameter cannot be parsed
     */
    Object[] buildParameters(Method method, Object sender, RegisteredCommand cmd, Object[] commandInputs, Map<Class<?>, Object> externalParams) throws ParamParsingException;

}
