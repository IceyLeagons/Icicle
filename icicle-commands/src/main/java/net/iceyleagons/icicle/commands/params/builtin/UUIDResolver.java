package net.iceyleagons.icicle.commands.params.builtin;

import net.iceyleagons.icicle.commands.annotations.CommandParamResolver;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 16, 2021
 */
@CommandParamResolver({UUID.class})
public class UUIDResolver implements CommandParameterResolverTemplate {

    @Override
    public Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender) {
        try {
            return UUID.fromString(arg);
        } catch (Exception e) {
            return null;
        }
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
