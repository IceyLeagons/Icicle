package net.iceyleagons.icicle.commands.middleware.impl;

import com.google.common.base.Strings;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.meta.PlayerOnly;
import net.iceyleagons.icicle.commands.middleware.CommandMiddleware;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.core.translations.TranslationService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @since Nov. 1, 2021
 */
@CommandMiddleware
public class PlayerOnlyMiddleware implements CommandMiddlewareTemplate {

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Method method, CommandSender sender, TranslationService translationService) throws Exception {
        if (method.isAnnotationPresent(PlayerOnly.class)) {
            handlePlayerOnly(sender, commandManager, translationService);
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Field field, CommandSender sender, TranslationService translationService) throws Exception {
        if (field.isAnnotationPresent(PlayerOnly.class)) {
            handlePlayerOnly(sender, commandManager, translationService);
        }

        return true;
    }

    private void handlePlayerOnly(CommandSender sender, CommandManager commandManager, TranslationService translationService) throws Exception {
        if (sender instanceof Player) return;

        String errorMsgKey = Strings.emptyToNull(commandManager.playerOnly());
        String msg = translationService.getTranslation(errorMsgKey, translationService.getLanguageProvider().getLanguage(sender), "&cYou have to be a player to execute this command!");

        throw new IllegalStateException(msg);
    }
}
