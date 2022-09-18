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

package net.iceyleagons.icicle.commands.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.Command;
import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.commands.CommandRegistry;
import net.iceyleagons.icicle.commands.exception.CommandNotFoundException;
import net.iceyleagons.icicle.commands.exception.CommandRegistrationException;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@RequiredArgsConstructor
public class CommandRegistryImpl implements CommandRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistryImpl.class);

    private final CommandManager commandManager;
    private final Map<String, Command> commands = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    private final Map<String, CommandManager> subCommands = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    @Override
    public void registerSubCommand(CommandManager subManager, String... aliases) throws CommandRegistrationException {
        if (!subManager.isSubCommand()) return;

        String cmd = subManager.getRoot();
        if (subCommands.containsKey(cmd)) {
            LOGGER.warn("CommandRegistry for main command: {}, already contains a sub-command: {}. Overwriting...", this.commandManager.getRoot(), cmd);
        }

        subCommands.put(cmd, subManager);

        for (String alias : aliases) {
            if (subCommands.containsKey(alias)) {
                LOGGER.warn("CommandRegistry for main command: {}, already contains an alias ({}) for sub-command: {}. Overwriting...", this.commandManager.getRoot(), alias, cmd);
            }

            subCommands.put(alias, subManager);
        }
    }

    @Override
    public void registerCommand(Method method, Object origin) throws CommandRegistrationException {
        if (!method.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.Command.class)) return;
        registerCommand(new CommandImpl(this.commandManager, method.getAnnotation(net.iceyleagons.icicle.commands.annotations.Command.class), method, origin));
    }

    private void registerCommand(Command command) throws CommandRegistrationException {
        for (String allCommandName : command.getAllCommandNames()) {
            if (commands.containsKey(allCommandName)) {
                LOGGER.warn("CommandRegistry for main command: {}, already contains a sub-command: {}. Overwriting...", this.commandManager.getRoot(), allCommandName);
            }

            commands.put(allCommandName.toLowerCase(), command);
        }
    }

    @Override
    public Set<Tuple<String, Command>> getAllChildCommands(String rootCommand) {
        final Set<Tuple<String, Command>> cmds = new ObjectArraySet<>(4); // Array set is PROBABLY faster because we're not comparing.

        this.commands.forEach((s, c) -> cmds.add(new UnmodifiableTuple<>(rootCommand + " " + s, c)));

        this.subCommands.forEach((s, manager) -> {
            String root = rootCommand + " " + s;
            cmds.addAll(manager.getCommandRegistry().getAllChildCommands(root));
        });

        return cmds;
    }

    @Override
    public Map<String, Command> getCommands() {
        return this.commands;
    }

    @Override
    public Map<String, CommandManager> getSubCommands() {
        return this.subCommands;
    }

    @Override
    public CommandManager getSubCommand(String cmd) throws CommandNotFoundException {
        String lower = cmd.toLowerCase();

        if (!subCommands.containsKey(lower)) throw new CommandNotFoundException(cmd);
        return subCommands.get(lower);
    }

    @Override
    public Command getCommand(String cmd) throws CommandNotFoundException {
        String lower = cmd.toLowerCase();

        if (!commands.containsKey(lower)) throw new CommandNotFoundException(cmd);
        return commands.get(lower);
    }
}
