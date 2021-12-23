package net.iceyleagons.icicle.commands.params;

import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 16, 2021
 */
public interface CommandParameterResolverTemplate extends TabCompleter {

    // TODO tabcomplete
    Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender);

}
