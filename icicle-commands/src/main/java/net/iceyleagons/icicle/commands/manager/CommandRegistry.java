package net.iceyleagons.icicle.commands.manager;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.command.CommandNotFoundException;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
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

    public RegisteredCommand getCommand(String cmd) throws CommandNotFoundException {
        String lower = cmd.toLowerCase();

        if (!commands.containsKey(lower)) throw new CommandNotFoundException(cmd);
        return commands.get(lower);
    }

    private static String[] getAliases(Method method) {
        String[] aliases = method.isAnnotationPresent(Alias.class) ? method.getAnnotation(Alias.class).value() : new String[0];
        return Arrays.stream(aliases).map(String::toLowerCase).toArray(String[]::new);
    }
}
