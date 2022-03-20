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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.command.CommandNotFoundException;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@RequiredArgsConstructor
public class CommandRegistry {

    private final RegisteredCommandManager commandManager;
    @Getter
    private final Map<String, RegisteredCommand> commands = new ConcurrentHashMap<>();
    @Getter
    private final Map<String, RegisteredCommandManager> subCommands = new ConcurrentHashMap<>();

    private static String[] getAliases(Method method) {
        String[] aliases = method.isAnnotationPresent(Alias.class) ? method.getAnnotation(Alias.class).value() : new String[0];
        return Arrays.stream(aliases).map(String::toLowerCase).toArray(String[]::new);
    }

    public Set<Map.Entry<String,RegisteredCommand>> getAllChildCommands(String rootCommand) {
        final Set<Map.Entry<String,RegisteredCommand>> cmds = new HashSet<>();

        this.commands.forEach((s, c) -> {
            cmds.add(Map.entry(rootCommand + " " + s, c));
        });

        this.subCommands.forEach((s, manager) -> {
            String root = rootCommand + " " + s;
            cmds.addAll(manager.getCommandRegistry().getAllChildCommands(root));
        });

        return cmds;
    }

    public void registerSubCommand(RegisteredCommandManager manager, String... aliases) {
        if (!manager.getCommandManager().isSubCommand()) return;
        subCommands.put(manager.getCommandManager().value(), manager);
        for (String alias : aliases) {
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
