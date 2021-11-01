package net.iceyleagons.icicle.commands;

import lombok.NonNull;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * @since Nov. 1, 2021
 */
public class CommandInjector {

    public static void injectCommand(@NonNull String command, @NonNull CommandExecutor commandExecutor, @NonNull JavaPlugin plugin) throws CommandInjectionException {
        injectCommand(command, commandExecutor, null, null, null, plugin);
    }

    public static void injectCommand(@NonNull String command, @NonNull CommandExecutor commandExecutor, TabCompleter tabCompleter, String usage, String description, @NonNull JavaPlugin plugin) throws CommandInjectionException {
        injectCommand(command, commandExecutor, tabCompleter, usage, description, null, null, Collections.emptyList(), plugin);
    }

    /**
     * Injects a command. What it means, that you don't have to enter it into the plugin.yml
     *
     * @param command           the command (required)
     * @param commandExecutor   the commandExecutor (required)
     * @param tabCompleter      the tabCompleter (optional)
     * @param usage             usage text (optional)
     * @param description       description of the command (optional)
     * @param permission        permission for the command (optional)
     * @param permissionMessage insufficient permission error messaga (optional)
     * @param aliases           aliases for the command (optional)
     * @throws CommandInjectionException if errors happen during the injection
     */
    public static void injectCommand(@NonNull String command, @NonNull CommandExecutor commandExecutor,
                                     TabCompleter tabCompleter, String usage, String description,
                                     String permission, String permissionMessage,
                                     List<String> aliases, @NonNull JavaPlugin plugin) throws CommandInjectionException {
        try {
            final Field bukkitCommandMap = ReflectionUtils.getField(plugin.getServer().getClass(), "commandMap", true);
            if (bukkitCommandMap == null) throw new CommandInjectionException(command, "CommandMap is unavailable");

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            Constructor<PluginCommand> c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            PluginCommand pluginCommand = c.newInstance(command, plugin);
            if (tabCompleter != null) pluginCommand.setTabCompleter(tabCompleter);
            if (usage != null) pluginCommand.setUsage(usage);
            if (aliases != null) pluginCommand.setAliases(aliases);
            if (description != null) pluginCommand.setDescription(description);
            if (permission != null) pluginCommand.setPermission(permission);
            if (permissionMessage != null) pluginCommand.setPermissionMessage(permissionMessage);
            pluginCommand.setExecutor(commandExecutor);
            pluginCommand.setAliases(Collections.emptyList());
            commandMap.register(command, pluginCommand);
        } catch (Exception e) {
            throw new CommandInjectionException(command, e);
        }
    }
}
