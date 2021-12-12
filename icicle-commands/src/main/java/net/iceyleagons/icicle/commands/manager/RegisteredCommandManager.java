package net.iceyleagons.icicle.commands.manager;

import com.google.common.base.Strings;
import lombok.Getter;
import net.iceyleagons.icicle.commands.CommandInjectionException;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.meta.Alias;
import net.iceyleagons.icicle.commands.command.CommandNotFoundException;
import net.iceyleagons.icicle.commands.command.RegisteredCommand;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    private String handleCommand(RegisteredCommand toExecute, String[] args) throws Exception {
        return toExecute.execute(new String[0]);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] args) {
        final TranslationService translationService = commandService.getTranslationService();
        final String cmd = command.getName().toLowerCase();
        if (!cmd.equalsIgnoreCase(this.commandManager.value())) return true;

        try {
            if (args.length < 1) {
                sender.sendMessage("Specify subcommand!");
                return true;
            }

            RegisteredCommand registeredCommand = this.commandRegistry.getCommand(args[0].toLowerCase());
            String[] newArgs = ArrayUtils.ignoreFirst(1, args);

            String response = handleCommand(registeredCommand, newArgs);
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
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', e.getMessage()));
            return true;
        }

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
