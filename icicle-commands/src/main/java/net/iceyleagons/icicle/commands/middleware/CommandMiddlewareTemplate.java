package net.iceyleagons.icicle.commands.middleware;

import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.core.translations.TranslationService;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @since Nov. 1, 2021
 */
public interface CommandMiddlewareTemplate {

    /**
     * @param commandManager
     * @param commandManagerClass
     * @param method
     * @return true if the command can proceed, false if issues arise.
     * @throws Exception
     */
    boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                      Method method, CommandSender sender, TranslationService translationService) throws Exception;

    /**
     * @param commandManager
     * @param commandManagerClass
     * @param field
     * @return true if the command can proceed, false if issues arise.
     * @throws Exception
     */
    boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                      Field field, CommandSender sender, TranslationService translationService) throws Exception;

}
