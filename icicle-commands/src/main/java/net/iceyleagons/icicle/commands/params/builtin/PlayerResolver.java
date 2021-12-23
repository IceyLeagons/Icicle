package net.iceyleagons.icicle.commands.params.builtin;

import net.iceyleagons.icicle.commands.annotations.CommandParamResolver;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.core.annotations.Autowired;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 16, 2021
 */
@CommandParamResolver({Player.class})
public class PlayerResolver implements CommandParameterResolverTemplate {

    private final Server server;

    @Autowired
    public PlayerResolver(Server server) {
        this.server = server;
    }

    @Override
    public Object resolveParameter(Class<?> type, RegisteredCommandManager manager, String arg, CommandSender commandSender) {
        //if (!type.equals(Player.class)) return null; // making sure

        // TODO UUID recognition and handling players with UUID
        return server.getPlayer(arg);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return server.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
    }
}
