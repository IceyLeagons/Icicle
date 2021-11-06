package net.iceyleagons.icicle.commands.manager;

import lombok.Getter;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.Alias;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.GetterCommand;
import net.iceyleagons.icicle.commands.annotations.SetterCommand;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.performance.PerformanceLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class RegisteredCommandManager {

    private final Application application;
    private final CommandService commandService;
    private final CommandManager commandManager;
    private final Class<?> clazz;
    private final Object origin;

    private final Map<String, RegisteredCommand> commands = new HashMap<>();

    public RegisteredCommandManager(Application application, CommandService commandService, CommandManager commandManager, Class<?> clazz, Object origin) {
        this.application = application;
        this.commandService = commandService;
        this.commandManager = commandManager;
        this.clazz = clazz;
        this.origin = origin;

        scanCommands();
    }

    private static RegisteredCommand createFieldCommand(Field field) {
        Class<?>[] paramTypes = new Class[1];
        paramTypes[0] = field.getType();

        String command = getMainCommandName(field);
        String[] aliases = getAliases(field);

        return new RegisteredCommand(paramTypes, command, aliases);
    }

    private static RegisteredCommand createMethodCommand(Method method) {
        Class<?>[] paramTypes = method.getParameterTypes();

        String command = getMainCommandName(method);
        String[] aliases = getAliases(method);

        return new RegisteredCommand(paramTypes, command, aliases);
    }

    private static String getMainCommandName(Method method) {
        String val = method.getAnnotation(Command.class).value();
        return (val.isEmpty() ? method.getName() : val).toLowerCase();
    }

    private static String getMainCommandName(Field field) {
        String val = field.getAnnotation(Command.class).value();
        return (val.isEmpty() ? field.getName() : val).toLowerCase();
    }

    private static String[] getAliases(Method method) {
        return method.isAnnotationPresent(Alias.class) ? method.getAnnotation(Alias.class).value() : new String[0];
    }

    private static String[] getAliases(Field field) {
        return field.isAnnotationPresent(Alias.class) ? field.getAnnotation(Alias.class).value() : new String[0];
    }

    private void scanCommands() {
        PerformanceLog.begin(this.application, "Scanning & Registering commands", RegisteredCommandManager.class);
        handleSubCommands();

        for (Method commandMethod : getCommandMethods()) {
            PerformanceLog.begin(this.application, "Registering command from method: " + commandMethod.getName(), RegisteredCommandManager.class);

            RegisteredCommand registeredCommand = createMethodCommand(commandMethod);

            PerformanceLog.end(this.application);
        }

        for (Field commandField : getCommandFields()) {
            PerformanceLog.begin(this.application, "Registering command from field: " + commandField.getName(), RegisteredCommandManager.class);

            RegisteredCommand registeredCommand = createFieldCommand(commandField);


            PerformanceLog.end(this.application);
        }

        PerformanceLog.end(this.application);
    }

    private Set<Method> getCommandMethods() {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Command.class))
                .peek(m -> m.setAccessible(true))
                .collect(Collectors.toSet());
    }

    private Set<Field> getCommandFields() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(GetterCommand.class) || f.isAnnotationPresent(SetterCommand.class))
                .peek(f -> f.setAccessible(true))
                .collect(Collectors.toSet());
    }

    private void handleSubCommands() {
        // TODO later (removed @SubCommandManager until then)
    }
}
