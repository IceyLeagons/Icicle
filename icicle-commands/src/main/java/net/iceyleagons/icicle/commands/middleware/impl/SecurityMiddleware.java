/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.commands.middleware.impl;

import net.iceyleagons.icicle.commands.annotations.manager.CommandManager;
import net.iceyleagons.icicle.commands.annotations.meta.Permission;
import net.iceyleagons.icicle.commands.exception.TranslatableException;
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

    private static final String KEY = "icicle.cmd.middleware.playeronly.err";

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Method method, CommandSender sender, TranslationService translationService) {
        if (method.isAnnotationPresent(Permission.class)) {
            handlePermission(method.getAnnotation(Permission.class), sender, commandManager);
        }

        return true;
    }

    @Override
    public boolean onCommand(CommandManager commandManager, Class<?> commandManagerClass, String commandName,
                             Field field, CommandSender sender, TranslationService translationService) {
        if (field.isAnnotationPresent(Permission.class)) {
            handlePermission(field.getAnnotation(Permission.class), sender, commandManager);
        }

        return true;
    }

    private void handlePermission(Permission permission, CommandSender sender,
                                  CommandManager commandManager) {

        String requiredPermission = permission.value();
        if (sender.hasPermission(requiredPermission)) return;
        //  String msg = translationService.getTranslation(errorMsgKey, translationService.getLanguageProvider().getLanguage(sender), "&cInsufficient permissions!",
        //         Map.of("permission", requiredPermission, "sender", sender.getName()));

        //throw new IllegalStateException(msg);
        throw new TranslatableException(KEY, "&cInsufficient permissions!", Map.of("permission", requiredPermission, "sender", sender.getName()));
    }
}
