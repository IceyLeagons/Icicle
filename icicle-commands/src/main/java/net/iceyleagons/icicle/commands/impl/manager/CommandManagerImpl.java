/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.commands.impl.manager;

import net.iceyleagons.icicle.commands.*;
import net.iceyleagons.icicle.commands.annotations.Alias;
import net.iceyleagons.icicle.commands.annotations.Command;
import net.iceyleagons.icicle.commands.annotations.manager.SubCommand;
import net.iceyleagons.icicle.commands.exception.CommandNotFoundException;
import net.iceyleagons.icicle.commands.exception.CommandRegistrationException;
import net.iceyleagons.icicle.commands.impl.CommandRegistryImpl;
import net.iceyleagons.icicle.commands.impl.ParameterHandlerImpl;
import net.iceyleagons.icicle.commands.middleware.CommandMiddlewareTemplate;
import net.iceyleagons.icicle.commands.utils.ArgUtils;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.exceptions.TranslatableException;
import net.iceyleagons.icicle.core.translations.TranslationService;

import java.lang.reflect.Method;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
public class CommandManagerImpl implements CommandManager {

    private final Application application;
    private final TranslationService translationService;
    private final CommandExecutionHandler commandExecutionHandler;
    private final CommandService commandService;
    private final CommandRegistry commandRegistry;
    private final ParameterHandler parameterHandler;

    private final Class<?> clazz;
    private final Object origin;

    private final String root;
    private final boolean isSubCommand;

    public CommandManagerImpl(CommandService commandService, Class<?> clazz, Object origin, String root, boolean isSubCommand) throws CommandRegistrationException {
        this.application = commandService.getApplication();
        this.translationService = commandService.getTranslationService();
        this.commandExecutionHandler = commandService.getExecutionHandler();
        this.commandService = commandService;
        this.commandRegistry = new CommandRegistryImpl(this);
        this.parameterHandler = new ParameterHandlerImpl(this, commandService);
        this.clazz = clazz;
        this.origin = origin;
        this.root = root;
        this.isSubCommand = isSubCommand;

        scanForCommands();
        if (!isSubCommand) {
            String[] aliases = this.clazz.isAnnotationPresent(Alias.class) ? this.clazz.getAnnotation(Alias.class).value() : new String[0];
            this.commandExecutionHandler.registerCommandManagerIntoContext(this.root, this, aliases);
        }
    }

    @Override
    public void onCommand(Object sender, String command, String[] args) throws Exception {
        final String cmd = command.toLowerCase();
        if (!cmd.equalsIgnoreCase(this.getRoot())) return;

        try {
            if (args.length < 1) {
                // TODO error
                return;
            }

            if (args[0].equalsIgnoreCase("help")) {
                // TODO
                return;
            }

            handleCommand(this.getCommandRegistry().getCommand(args[0].toLowerCase()), sender, args);
        } catch (CommandNotFoundException e) {
            try {
                // Second chance
                handleSubCommand(this.getCommandRegistry().getSubCommand(args[0].toLowerCase()), sender, args);
            } catch (CommandNotFoundException e2) {
                // Giving up
                // TODO
            }
        } catch (TranslatableException e) {
            this.commandExecutionHandler.sendToSender(
                    sender,
                    translationService.getTranslation(e.getKey(), translationService.getLanguageProvider().getLanguage(sender), e.getDefaultValue(), e.getParams())
            );
        } catch (Exception e) {
            this.commandExecutionHandler.sendErrorToSender(sender, e.getMessage());
        }
    }

    private void handleCommand(net.iceyleagons.icicle.commands.Command cmd, Object sender, String[] args) throws Exception {
        for (CommandMiddlewareTemplate middleware : commandService.getMiddlewareStore().getElementsArray()) {
            if (!middleware.onCommand(this, this.clazz, args[0].toLowerCase(), cmd.getMethod(), sender, this.translationService)) {
                return;
            }
        }

        Object[] parameters = parameterHandler.getParameters(cmd, ArgUtils.stripFirst(args), sender);
        String response = cmd.execute(parameters);

        if (response == null) return;
        if (cmd.isSupplyingTranslationKeys()) {
            commandExecutionHandler.sendToSender(
                    sender,
                    translationService.getTranslation(response, translationService.getLanguageProvider().getLanguage(sender), "")
            );
        } else {
            commandExecutionHandler.sendToSender(sender, response);
        }
    }

    private void handleSubCommand(CommandManager subCommand, Object sender, String[] args) throws Exception {
        subCommand.onCommand(sender, subCommand.getRoot(), ArgUtils.stripFirst(args));
    }

    private void scanForCommands() throws CommandRegistrationException {
        for (Method declaredMethod : this.clazz.getDeclaredMethods()) {
            if (!declaredMethod.isAnnotationPresent(Command.class)) continue;
            commandRegistry.registerCommand(declaredMethod, origin);
        }

        for (SubCommand subCommand : this.clazz.getAnnotationsByType(SubCommand.class)) {
            final Class<?> clazz = subCommand.value();
            final Object sc = application.getBeanManager().getBeanRegistry().getBeanNullable(clazz);

            application.getBeanManager().getBeanRegistry().unregisterBean(clazz);

            if (!(sc instanceof CommandManager)) {
                throw new IllegalStateException("Annotation value: " + clazz.getName() + " points to a non existing SubCommand manager in " + this.clazz.getName() + " .");
            }
            commandRegistry.registerSubCommand((CommandManager) sc);
        }
    }

    @Override
    public String getRoot() {
        return this.root;
    }

    @Override
    public boolean isSubCommand() {
        return this.isSubCommand;
    }

    @Override
    public CommandService getCommandService() {
        return this.commandService;
    }

    @Override
    public CommandRegistry getCommandRegistry() {
        return this.commandRegistry;
    }
}
