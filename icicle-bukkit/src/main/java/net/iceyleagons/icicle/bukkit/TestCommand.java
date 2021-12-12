package net.iceyleagons.icicle.bukkit;

import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;

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
    public String asd2() {
        return "Executed2";
    }
}
