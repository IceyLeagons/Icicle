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

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.params.ParameterInfo;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverRegistry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class holds all the necessary information about a command.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 * @see Command
 * @see net.iceyleagons.icicle.commands.annotations.CommandParameter
 */
@Getter
@RequiredArgsConstructor
public class RegisteredCommand {

    /**
     * The name of the command. The actual thing the user has to enter
     */
    private final String name;

    /**
     * The description of the command's function.
     */
    private final String description;

    /**
     * Aliases of the command. In some contexts this may not be supported!
     */
    private final String[] aliases;

    /**
     * The method of the command to invoke at execution.
     */
    private final Method method;

    /**
     * Parent bean of the command. The actual {@link net.iceyleagons.icicle.commands.annotations.CommandContainer}
     */
    private final Object origin;

    /**
     * Parameters of the command mapped to {@link ParameterInfo} objects.
     * The order follows the order of the parameters of the method.
     */
    private final List<ParameterInfo> parameters = new ObjectArrayList<>();

    /**
     * Creates a new RegisteredCommand instance from the given inputs.
     * This method will also check, whether the parameters in the method have appropriate parameter resolvers.
     *
     * @param method the method of the command
     * @param bean the parent bean of the command, the actual {@link net.iceyleagons.icicle.commands.annotations.CommandContainer} instance
     * @param parameterResolverRegistry the {@link ParameterResolverRegistry} to use to check for available resolvers
     * @return the resulting instance
     * @throws IllegalStateException – if no parameter resolvers are found for the parameter
     */
    public static RegisteredCommand from(Method method, Object bean, ParameterResolverRegistry parameterResolverRegistry) {
        Command cmd = method.getAnnotation(Command.class);
        RegisteredCommand registeredCommand = new RegisteredCommand(cmd.name(), cmd.description(), new String[0], method, bean);
        registeredCommand.getParameters().addAll(Arrays.stream(method.getParameters())
                .map(param -> ParameterInfo.from(param, parameterResolverRegistry))
                .filter(Objects::nonNull)
                .toList());
        return registeredCommand;
    }

    /**
     * Utility method to execute the command
     *
     * @param arguments the arguments to call the command with. Ideally from {@link net.iceyleagons.icicle.commands.params.InvocationParameterBuilder}
     * @return the return of the method (or null)
     * @throws Exception if anything happens during execution
     */
    @Nullable
    public Object execute(Object[] arguments) throws Exception {
        return this.method.invoke(origin, arguments);
    }
}
