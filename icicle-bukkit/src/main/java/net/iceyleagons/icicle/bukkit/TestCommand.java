package net.iceyleagons.icicle.bukkit;

import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.params.CommandSender;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import org.bukkit.entity.Player;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@CommandManager("test")
public class TestCommand {

    @Command(value = "one", returnsTranslationKey = false)
    public String asd() {
        return "Executed!";
    }

    @Command(value = "two", returnsTranslationKey = false)
    public String asd2(@CommandSender Player player) {
        return "Executed2 sender: " + player.getName();
    }

    @Command(value = "three", returnsTranslationKey = false)
    public String asd2(@CommandSender Player player, boolean arg) {
        return "Executed2 sender: " + player.getName() + " | arg: " + arg;
    }

    @Command(value = "four", returnsTranslationKey = false)
    public String asd2(@CommandSender Player player, String arg, @Optional Player argOpt) {
        return "Executed2 sender: " + player.getName() + " | arg: " + arg + " opt: " + argOpt;
    }
}
