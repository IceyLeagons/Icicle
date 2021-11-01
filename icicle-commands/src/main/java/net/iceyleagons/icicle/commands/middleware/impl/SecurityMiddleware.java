package net.iceyleagons.icicle.commands.middleware.impl;

import com.google.common.base.Strings;
import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.Permission;
import net.iceyleagons.icicle.commands.exception.InsufficientPermissionsException;
import net.iceyleagons.icicle.commands.middleware.CommandMiddleware;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.core.translations.TranslationService;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @since Nov. 1, 2021
 */
@CommandMiddleware
public class SecurityMiddleware implements CommandMiddlewareTemplate {

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Method method, CommandSender sender, TranslationService translationService) throws Exception {
        if (method.isAnnotationPresent(Permission.class)) {
            handlePermission(method.getAnnotation(Permission.class), sender, commandManager, translationService);
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Field field, CommandSender sender, TranslationService translationService) throws Exception {
        if (field.isAnnotationPresent(Permission.class)) {
            handlePermission(field.getAnnotation(Permission.class), sender, commandManager, translationService);
        }

        return true;
    }

    private void handlePermission(Permission permission, CommandSender sender,
                                  CommandManager commandManager, TranslationService translationService) throws Exception {
        String requiredPermission = permission.value();
        if (sender.hasPermission(requiredPermission)) return;

        String errorMsgKey = Strings.emptyToNull(commandManager.permissionError());
        String msg = translationService.getTranslation(errorMsgKey, translationService.getLanguageProvider().getLanguage(sender), "&cInsufficient permissions!",
                Map.of("permission", requiredPermission, "sender", sender.getName()));

        throw new InsufficientPermissionsException(msg);
    }
}
