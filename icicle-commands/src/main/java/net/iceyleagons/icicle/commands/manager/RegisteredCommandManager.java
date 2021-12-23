package net.iceyleagons.icicle.commands.manager;

import com.google.common.base.Strings;
import lombok.Getter;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;
import net.iceyleagons.icicle.commands.annotations.params.FlagOptional;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import net.iceyleagons.icicle.commands.command.CommandNotFoundException;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.core.utils.Defaults;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Getter
public class RegisteredCommandManager implements CommandExecutor, TabCompleter {

    private final Application application;
    private final CommandService commandService;
    private final CommandManager commandManager;
    private final CommandRegistry commandRegistry;
    private final Class<?> clazz;
    private final Object origin;

    public RegisteredCommandManager(Application application, CommandService commandService, CommandManager commandManager, Class<?> clazz, Object origin) throws CommandInjectionException {
        this.application = application;
        this.commandRegistry = new CommandRegistry(this);
        this.commandService = commandService;
        this.commandManager = commandManager;
        this.clazz = clazz;
        this.origin = origin;

        scanForCommands();
        this.getCommandService().getInjector().injectCommand(commandManager.value().toLowerCase(), this, this, null, null, null, null, Arrays.asList(clazz.isAnnotationPresent(Alias.class) ? clazz.getAnnotation(Alias.class).value() : new String[0]));
    }

    private void scanForCommands() throws CommandInjectionException {
        for (Method declaredMethod : this.clazz.getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Command.class)) continue;

            commandRegistry.registerCommand(declaredMethod, origin);
        }
    }

    private Object[] getParams(Parameter[] parameters, String[] args, CommandSender commandSender) throws IllegalArgumentException {
        Object[] params = new Object[parameters.length];
        int argsCounter = 0;

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            if (param.getType().isArray() && param.getType().equals(String.class)) {
                Object[] array = new Object[args.length - argsCounter];
                for (int j = argsCounter; j < args.length; j++) {
                    array[j - argsCounter] = args[argsCounter];
                }

                params[i] = array; // TODO this needs testing
                break;
            } else if (param.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class)) {
                params[i] = param.getType().isInstance(commandSender) ? param.getType().cast(commandSender) : null;
            } else if (param.isAnnotationPresent(FlagOptional.class)) {

            } else if (param.isAnnotationPresent(Optional.class)) {
                if (argsCounter < args.length) {
                    params[i] = this.commandService.resolveParameter(param.getType(), this, args[argsCounter++], commandSender);
                    continue;
                }
                params[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
            } else {
                if (argsCounter < args.length) {
                    params[i] = this.commandService.resolveParameter(param.getType(), this, args[argsCounter++], commandSender);
                    continue;
                }

                throw new IllegalArgumentException("Too few arguments!");
            }
        }

        return params;
    }

    private String handleCommand(RegisteredCommand toExecute, String[] args, CommandSender commandSender) throws Exception {
        try {
            Object[] params = getParams(toExecute.getMethod().getParameters(), args, commandSender);
            return toExecute.execute(params);
        } catch (IllegalArgumentException e) {
            String usage;
            if (toExecute.getUsage() == null) {
                usage = toExecute.getDefaultUsage();
            } else {
                final TranslationService translationService = commandService.getTranslationService();
                final Usage usg = toExecute.getUsage();

                String key = Strings.emptyToNull(usg.key());
                usage = translationService.getTranslation(key, translationService.getLanguageProvider().getLanguage(commandSender), usg.defaultValue(),
                        Map.of("cmd", toExecute.getCommandName(), "sender", commandSender.getName()));
            }

            throw new IllegalArgumentException("Too few arguments!\nUsage: " + usage);
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        final TranslationService translationService = commandService.getTranslationService();
        final String cmd = command.getName().toLowerCase();
        commandService.getParamResolvers().forEach((s, s1) -> {
            System.out.println(s.getName() + " --> " + s1.toString());
        });
        if (!cmd.equalsIgnoreCase(this.commandManager.value())) return true;

        try {
            if (args.length < 1) {
                sender.sendMessage("Specify subcommand!");
                return true;
            }

            RegisteredCommand registeredCommand = this.commandRegistry.getCommand(args[0].toLowerCase());
            for (CommandMiddlewareTemplate middleware : commandService.getMiddlewareStore().getMiddlewares()) {
                if (!middleware.onCommand(this.commandManager, this.clazz, args[0].toLowerCase(), registeredCommand.getMethod(), sender, this.commandService.getTranslationService())) {
                    return true;
                }
            }

            String[] newArgs = ArrayUtils.ignoreFirst(1, args);

            String response = handleCommand(registeredCommand, newArgs, sender);
            if (registeredCommand.isSuppliesTranslationKey()) {
                response = translationService.getTranslation(response, translationService.getLanguageProvider().getLanguage(sender), "");
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', response));
        } catch (CommandNotFoundException e) {
            String errorMsgKey = Strings.emptyToNull(commandManager.notFound());
            String msg = translationService.getTranslation(errorMsgKey, translationService.getLanguageProvider().getLanguage(sender), "&cCommand &b{cmd} &cnot found!",
                    Map.of("cmd", e.getCommand(), "sender", sender.getName()));

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        } catch (Exception e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', ChatColor.RED + e.getMessage()));
            return true;
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], commandRegistry.getCommands().keySet(), completions);
            Collections.sort(completions);
        } else {
            if (args.length > 1 && commandRegistry.getCommands().containsKey(args[0])) {
                try {
                    return commandRegistry.getCommand(args[0]).onTabComplete(sender, command, alias, ArrayUtils.ignoreFirst(1, args));
                } catch (Exception ignored) {}
            }
        }

        return completions;
    }
}
