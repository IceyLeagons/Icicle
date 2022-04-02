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

import com.google.common.base.Strings;
import lombok.Getter;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.Predictor;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.manager.SubCommand;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.annotations.meta.Description;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;
import net.iceyleagons.icicle.commands.annotations.params.FlagOptional;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import net.iceyleagons.icicle.commands.exception.CommandNotFoundException;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;
import net.iceyleagons.icicle.commands.exception.TranslatableException;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.core.utils.Defaults;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import net.iceyleagons.icicle.utilities.ListUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class RegisteredCommandManager implements CommandExecutor, TabCompleter {

    private static final String TOO_FEW_ARGS_KEY = "icicle.cmd.err.fewargs";
    private static final String SUB_COMMAND_KEY = "icicle.cmd.err.subcmd";
    private static final String HELP_TEXT = "icicle.cmd.help.info";
    private static final String HELP_INDEX = "icicle.cmd.help.index";

    private final Application application;
    private final CommandService commandService;
    private final CommandManager commandManager;
    private final CommandRegistry commandRegistry;
    private final Class<?> clazz;
    private final Object origin;

    public RegisteredCommandManager(Application application, CommandService commandService, CommandManager commandManager, Class<?> clazz, Object origin) throws CommandInjectionException {
        this(application, commandService, commandManager, clazz, origin, false);
    }

    public RegisteredCommandManager(Application application, CommandService commandService, CommandManager commandManager, Class<?> clazz, Object origin, boolean subCommand) throws CommandInjectionException {
        this.application = application;
        this.commandRegistry = new CommandRegistry(this);
        this.commandService = commandService;
        this.commandManager = commandManager;
        this.clazz = clazz;
        this.origin = origin;

        scanForCommands();
        if (!subCommand) {
            this.getCommandService().getInjector().injectCommand(commandManager.value().toLowerCase(), this, this, null, null, null, null, Arrays.asList(clazz.isAnnotationPresent(Alias.class) ? clazz.getAnnotation(Alias.class).value() : new String[0]));
        }
    }

    private void scanForCommands() throws CommandInjectionException {
        for (Method declaredMethod : this.clazz.getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Command.class)) continue;

            commandRegistry.registerCommand(declaredMethod, origin);
        }
        for (SubCommand subCommand : this.clazz.getAnnotationsByType(SubCommand.class)) {
            Class<?> clazz = subCommand.value();
            Object sc = application.getBeanManager().getBeanRegistry().getBeanNullable(clazz);
            application.getBeanManager().getBeanRegistry().unregisterBean(clazz);

            if (!(sc instanceof RegisteredCommandManager)) {
                throw new IllegalStateException("Annotation value: " + clazz.getName() + " points to a non existing SubCommand manager in " + this.clazz.getName() + " .");
            }

            commandRegistry.registerSubCommand((RegisteredCommandManager) sc);
        }

    }

    private static String join(String[] args, int startFrom) {
        StringBuilder sb = new StringBuilder();
        for (int i = startFrom; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        return sb.toString();
    }

    private Object[] getParams(RegisteredCommand cmd, String[] args, CommandSender commandSender) throws Exception {
        final Parameter[] parameters = cmd.getMethod().getParameters();

        Object[] params = new Object[parameters.length];
        int argsCounter = 0;
        String wholeArgs = join(args, 0);

        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            if (param.getType().isArray()) {
                // TODO VERY BROKEN FIX!!!!!!
                Object[] array = new Object[args.length - argsCounter];
                for (int j = argsCounter; j < args.length; j++) {
                    array[j - argsCounter] = this.commandService.resolveParameter(param.getType(), param, this, args[argsCounter], commandSender);
                }

                System.out.println(Arrays.toString(array));
                params[i] = array; // TODO this needs testing
                break;
            } else if (param.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class)) {
                params[i] = param.getType().isInstance(commandSender) ? param.getType().cast(commandSender) : null;
            } else if (param.isAnnotationPresent(FlagOptional.class)) {
                FlagOptional flagOptional = param.getAnnotation(FlagOptional.class);
                String flag = flagOptional.value();
                Matcher matcher = Pattern.compile("-" + flag + "(.*?(?= -| $|$))").matcher(wholeArgs);
                if (matcher.find()) {
                    String value = matcher.group(1).stripIndent();
                    if (value == null) {
                        if (param.getType().equals(boolean.class) || param.getType().equals(Boolean.class)) {
                            params[i] = true;
                            continue;
                        }
                        params[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
                        continue;
                    }

                    params[i] = this.commandService.resolveParameter(param.getType(), param, this, value, commandSender);
                    continue;
                }

                params[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
            } else if (param.isAnnotationPresent(Optional.class)) {
                if (argsCounter < args.length) {
                    params[i] = this.commandService.resolveParameter(param.getType(), param, this, args[argsCounter++], commandSender);
                    continue;
                }
                params[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
            } else {
                if (argsCounter < args.length) {
                    params[i] = this.commandService.resolveParameter(param.getType(), param, this, args[argsCounter++], commandSender);
                    continue;
                }

                throw new TranslatableException(TOO_FEW_ARGS_KEY, "Too few arguments! Usage: {usage}", Map.of("usage", getUsage(cmd, commandSender)));
            }
        }

        return params;
    }

    private String getUsage(RegisteredCommand toExecute, CommandSender commandSender) {
        String usage;
        if (toExecute.getUsage() == null) {
            usage = toExecute.getDefaultUsage(this.commandManager.value());
        } else {
            final TranslationService translationService = commandService.getTranslationService();
            final Usage usg = toExecute.getUsage();

            String key = Strings.emptyToNull(usg.key());
            usage = translationService.getTranslation(key, translationService.getLanguageProvider().getLanguage(commandSender), usg.defaultValue(),
                    Map.of("cmd", toExecute.getCommandName(), "sender", commandSender.getName()));
        }

        return usage;
    }

    private boolean handleCommand(RegisteredCommand registeredCommand, TranslationService translationService, CommandSender sender, String[] args) throws Exception {
        for (CommandMiddlewareTemplate middleware : commandService.getMiddlewareStore().getMiddlewares()) {
            if (!middleware.onCommand(this.commandManager, this.clazz, args[0].toLowerCase(), registeredCommand.getMethod(), sender, this.commandService.getTranslationService())) {
                return true;
            }
        }

        String[] newArgs = ArrayUtils.ignoreFirst(1, args);
        Object[] params = getParams(registeredCommand, newArgs, sender);

        String response = registeredCommand.execute(params);
        if (response == null) return true;

        if (registeredCommand.isSuppliesTranslationKey()) {
            response = translationService.getTranslation(response, translationService.getLanguageProvider().getLanguage(sender), "");
        }

        sender.sendMessage(color(response));
        return true;
    }

    private boolean handleSubCommand(RegisteredCommandManager manager, @NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String[] args) {
        String[] newArgs = ArrayUtils.ignoreFirst(1, args);
        return manager.onCommand(sender, command, manager.getCommandManager().value(), newArgs);
    }

    private void printHelp(CommandSender sender, int page) {
        final TranslationService translationService = this.commandService.getTranslationService();
        final List<Map.Entry<String, RegisteredCommand>> commands = ListUtils.toList(this.getCommandRegistry().getAllChildCommands(this.commandManager.value()));

        int pages = commands.size() / 10;

        if (pages == 0) pages = 1;
        if (page > pages) page = pages;

        String title = translationService.getTranslation(HELP_TEXT, translationService.getLanguageProvider().getLanguage(sender), "&6Help for &b {cmd}", Map.of("cmd", this.getCommandManager().value()));
        String lines = ChatColor.GOLD + StringUtils.repeat("=", title.length()/4);
        String index = translationService.getTranslation(HELP_TEXT, translationService.getLanguageProvider().getLanguage(sender), "{lines} &fHelp index: {current}/{max} {lines}",
                Map.of("lines", lines,
                        "current", String.valueOf(page + 1),
                        "max", String.valueOf(pages)));

        sender.sendMessage(color(title));
        sender.sendMessage(color(index));
        sender.sendMessage(" ");

        int p2 = (page + 1) * 10;
        for (int i = page * 10; i < Math.min(commands.size(), p2); i++) {
            Map.Entry<String, RegisteredCommand> entry = commands.get(i);
            Description description = entry.getValue().getDescription();

            if (description == null) {
                sender.sendMessage(ChatColor.GOLD + "/" + entry.getKey() + ChatColor.WHITE + " - N/A");
                continue;
            }

            String descriptionText = translationService.getTranslation(description.key(), translationService.getLanguageProvider().getLanguage(sender), "");

            sender.sendMessage(ChatColor.GOLD + "/" + entry.getKey() + ChatColor.WHITE + " - " + descriptionText);
        }
        sender.sendMessage(" ");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        final TranslationService translationService = commandService.getTranslationService();
        final String cmd = label.toLowerCase();
        if (!cmd.equalsIgnoreCase(this.commandManager.value())) return true;

        try {
            if (args.length < 1) {
                sender.sendMessage(color(
                        translationService.getTranslation(SUB_COMMAND_KEY, translationService.getLanguageProvider().getLanguage(sender), "&cPlease specify subcommand.")
                ));
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                // TODO Possible number format exception!!!!
                printHelp(sender, args.length < 2 ? 0 : Integer.parseInt(args[1]));
                return true;
            }

            RegisteredCommand registeredCommand = this.commandRegistry.getCommand(args[0].toLowerCase());
            return handleCommand(registeredCommand, translationService, sender, args);
        } catch (CommandNotFoundException e) {
            try {
                RegisteredCommandManager registeredCommand = this.commandRegistry.getSubCommand(args[0].toLowerCase());
                return handleSubCommand(registeredCommand, sender, command, args);
            } catch (CommandNotFoundException e2) {
                String errorMsgKey = Strings.emptyToNull(commandManager.notFound());
                AtomicReference<String> prediction = new AtomicReference<>("");
                Predictor.predict(this, args).ifPresent(r -> prediction.set(r.getDefaultUsage(this.commandManager.value())));

                String msg = translationService.getTranslation(errorMsgKey, translationService.getLanguageProvider().getLanguage(sender), "&cCommand &b{cmd} &cnot found! Did you mean {prediction}?",
                        Map.of("cmd", e.getCommand(), "sender", sender.getName(), "prediction", prediction.get()));

                sender.sendMessage(color(msg));
            }
        } catch (TranslatableException e) {
            String msg = translationService.getTranslation(e.getKey(), translationService.getLanguageProvider().getLanguage(sender), e.getDefaultValue(), e.getVariables());
            sender.sendMessage(color(msg));
            return true;
        } catch (Exception e) {
            if (this.commandManager.printExceptionStackTrace())
                e.printStackTrace();

            sender.sendMessage(color( ChatColor.RED + e.getMessage()));
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
                } catch (Exception ignored) {
                }
            }
        }

        return completions;
    }

    private static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
