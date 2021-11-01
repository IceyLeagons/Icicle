package net.iceyleagons.icicle.commands.manager;

import lombok.Getter;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;

@Getter
public class RegisteredCommandManager {

    private final CommandService commandService;
    private final CommandManager commandManager;
    private final Class<?> clazz;

    public RegisteredCommandManager(CommandService commandService, CommandManager commandManager, Class<?> clazz) {
        this.commandService = commandService;
        this.commandManager = commandManager;
        this.clazz = clazz;
    }
}
