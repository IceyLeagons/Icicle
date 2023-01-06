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

package net.iceyleagons.icicle.commands.manager;

import net.iceyleagons.icicle.commands.params.InvocationParameterBuilder;
import net.iceyleagons.icicle.commands.params.resolvers.ParameterResolverRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * A command service handles all the commands and command containers inside Icicle.
 * All application (which use icicle-commands) has exactly one instance (implementation) of it.
 * Default implementation is: {@link CommandServiceImpl}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 03, 2023
 */
public interface CommandService {

    /**
     * Method to actually call a command (if exists that is)
     *
     * @param name the command to call
     * @param sender the command sender
     * @param commandInputs the command arguments (in order of the parameters)
     * @param externalParams any external params to include for autowiring (for ex. command event, etc.)
     * @return the result of the command (return of those methods, depending on the ctx this can be useful) or null
     * @throws Exception if anything goes bad while exectuing the command
     */
    @Nullable
    Object execute(String name, Object sender, Object[] commandInputs, Map<Class<?>, Object> externalParams) throws Exception; // params can be SlashCommandInteractionEvent in case of JDA or Command etc. in MC

    /**
     * Registers a new command container into the service
     *
     * @param container the container original type (used because of proxying)
     * @param bean the actual instance of the container
     */
    void registerCommandContainer(Class<?> container, Object bean);

    /**
     * @return the {@link CommandRegistry} used by the command service
     */
    CommandRegistry getCommandRegistry();

    /**
     * @return the {@link InvocationParameterBuilder} instance used by the command service
     */
    InvocationParameterBuilder getParameterBuilder();

    /**
     * @return the {@link ParameterResolverRegistry} used by the command service
     */
    ParameterResolverRegistry getParameterResolverRegistry();

}
