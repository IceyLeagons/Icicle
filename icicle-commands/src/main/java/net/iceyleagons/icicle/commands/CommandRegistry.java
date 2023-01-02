/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

import net.iceyleagons.icicle.commands.exception.CommandNotFoundException;
import net.iceyleagons.icicle.commands.exception.CommandRegistrationException;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
public interface CommandRegistry {

    void registerSubCommand(CommandManager subManager, String... aliases) throws CommandRegistrationException;

    void registerCommand(Method method, Object origin) throws CommandRegistrationException;

    Command getRootCommand();

    void setRootCommand(Method method, Object origin);

    Set<Tuple<String, Command>> getAllChildCommands(String rootCommand);

    Map<String, Command> getCommands();

    Map<String, CommandManager> getSubCommands();

    CommandManager getSubCommand(String cmd) throws CommandNotFoundException;

    Command getCommand(String cmd) throws CommandNotFoundException;
}
