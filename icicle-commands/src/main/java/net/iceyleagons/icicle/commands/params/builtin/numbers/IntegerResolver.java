package net.iceyleagons.icicle.commands.params.builtin.numbers;

import net.iceyleagons.icicle.commands.annotations.CommandParamResolver;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 16, 2021
 */
@CommandParamResolver({int.class, Integer.class})
public class IntegerResolver implements CommandParameterResolverTemplate {

    @Override
    public Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender) {
        return isInt(arg) ? Integer.parseInt(arg) : null;
    }

    private static boolean isInt(String arg) {
        try {
            Integer.parseInt(arg);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.singletonList("0");
    }
}
