/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;
import net.iceyleagons.icicle.commands.exception.CommandNotFoundException;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@RequiredArgsConstructor
public class CommandRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandRegistry.class);

    private final RegisteredCommandManager commandManager;

    @Getter
    private final Map<String, RegisteredCommand> commands = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    @Getter
    private final Map<String, RegisteredCommandManager> subCommands = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    private static String[] getAliases(Method method) {
        String[] aliases = method.isAnnotationPresent(Alias.class) ? method.getAnnotation(Alias.class).value() : new String[0];
        return Arrays.stream(aliases).map(String::toLowerCase).toArray(String[]::new);
    }

    public Set<Tuple<String, RegisteredCommand>> getAllChildCommands(String rootCommand) {
        final Set<Tuple<String, RegisteredCommand>> cmds = new ObjectArraySet<>(4); // Array set is PROBABLY faster because we're not comparing.

        this.commands.forEach((s, c) -> cmds.add(new UnmodifiableTuple<>(rootCommand + " " + s, c)));

        this.subCommands.forEach((s, manager) -> {
            String root = rootCommand + " " + s;
            cmds.addAll(manager.getCommandRegistry().getAllChildCommands(root));
        });

        return cmds;
    }

    public void registerSubCommand(RegisteredCommandManager manager, String... aliases) {
        if (!manager.getCommandManager().isSubCommand()) return;

        String cmd = manager.getCommandManager().value();
        if (subCommands.containsKey(cmd)) {
            LOGGER.warn("CommandRegistry for main command: {}, already contains a sub-command: {}. Overwriting...", this.commandManager.getCommandManager().value(), cmd);
        }

        subCommands.put(cmd, manager);

        for (String alias : aliases) {
            if (subCommands.containsKey(alias)) {
                LOGGER.warn("CommandRegistry for main command: {}, already contains an alias ({}) for sub-command: {}. Overwriting...", this.commandManager.getCommandManager().value(), alias, cmd);
            }

            subCommands.put(alias, manager);
        }
    }

    public void registerCommand(Method method, Object origin) throws CommandInjectionException {
        if (!method.isAnnotationPresent(Command.class)) return;

        String cmd = method.getAnnotation(Command.class).value().toLowerCase();
        String[] aliases = getAliases(method);

        RegisteredCommand registeredCommand = new RegisteredCommand(this.commandManager, method.getParameterTypes(), cmd, method.getAnnotation(Command.class).returnsTranslationKey(), aliases, method, origin);
        registerCommand(registeredCommand);
    }

    public void registerCommand(RegisteredCommand command) throws CommandInjectionException {
        for (String allCommandName : command.getAllCommandNames()) {
            if (commands.containsKey(allCommandName)) {
                LOGGER.warn("CommandRegistry for main command: {}, already contains a sub-command: {}. Overwriting...", this.commandManager.getCommandManager().value(), allCommandName);
            }

            commands.put(allCommandName.toLowerCase(), command);
        }
    }

    public RegisteredCommandManager getSubCommand(String cmd) throws CommandNotFoundException {
        String lower = cmd.toLowerCase();

        if (!subCommands.containsKey(lower)) throw new CommandNotFoundException(cmd);
        return subCommands.get(lower);
    }

    public RegisteredCommand getCommand(String cmd) throws CommandNotFoundException {
        String lower = cmd.toLowerCase();

        if (!commands.containsKey(lower)) throw new CommandNotFoundException(cmd);
        return commands.get(lower);
    }
}
