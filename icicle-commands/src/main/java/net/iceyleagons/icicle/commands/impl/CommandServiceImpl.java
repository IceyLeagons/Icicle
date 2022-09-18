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

package net.iceyleagons.icicle.commands.impl;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.iceyleagons.icicle.commands.CommandExecutionHandler;
import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.exception.ParamParsingException;
import net.iceyleagons.icicle.commands.exception.ParameterValidationException;
import net.iceyleagons.icicle.commands.middleware.MiddlewareStore;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.commands.params.ParameterResolverStore;
import net.iceyleagons.icicle.commands.validators.CommandValidatorTemplate;
import net.iceyleagons.icicle.commands.validators.ValidatorStore;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.bean.Autowired;
import net.iceyleagons.icicle.core.annotations.service.Service;
import net.iceyleagons.icicle.core.translations.TranslationService;
import net.iceyleagons.icicle.utilities.Defaults;
import net.iceyleagons.icicle.utilities.lang.Internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@Service
public class CommandServiceImpl implements CommandService {

    private final Application application;
    private final TranslationService translationService;
    private final CommandExecutionHandler commandExecutionHandler;
    private final MiddlewareStore middlewareStore;
    private final ValidatorStore validatorStore;
    private final ParameterResolverStore parameterResolverStore;

    private final Set<CommandManager> commandManagers = new ObjectOpenHashSet<>();

    @Autowired
    public CommandServiceImpl(Application application, TranslationService translationService, CommandExecutionHandler commandExecutionHandler) {
        this.application = application;
        this.translationService = translationService;
        this.commandExecutionHandler = commandExecutionHandler;

        this.middlewareStore = new MiddlewareStore();
        this.validatorStore = new ValidatorStore();
        this.parameterResolverStore = new ParameterResolverStore();
    }

    @Internal
    @Override
    public Object resolveParameter(Class<?> type, Parameter parameter, CommandManager manager, String arg, Object sender) throws ParamParsingException, ParameterValidationException {
        final Map<Class<? extends Annotation>, CommandValidatorTemplate> validators = this.getValidatorStore().getElements();
        final Map<Class<?>, CommandParameterResolverTemplate> resolvers = this.getParameterResolverStore().getElements();

        for (Annotation annotation : parameter.getAnnotations()) {
            final Class<? extends Annotation> aType = annotation.annotationType();
            if (validators.containsKey(aType)) {
                validators.get(aType).validate(parameter, arg);
            }
        }

        if (arg == null || arg.isEmpty() || type == null || !resolvers.containsKey(type)) {
            return Defaults.DEFAULT_TYPE_VALUES.getOrDefault(type, null);
        }

        Object result = resolvers.get(type).resolveParameter(type, manager, arg, sender);
        return (Defaults.DEFAULT_TYPE_VALUES.containsKey(type) && result == null) ? Defaults.DEFAULT_TYPE_VALUES.get(type) : result;
    }

    @Override
    public Set<CommandManager> getCommandManagers() {
        return this.commandManagers;
    }

    @Override
    public ParameterResolverStore getParameterResolverStore() {
        return this.parameterResolverStore;
    }

    @Override
    public MiddlewareStore getMiddlewareStore() {
        return this.middlewareStore;
    }

    @Override
    public ValidatorStore getValidatorStore() {
        return this.validatorStore;
    }

    @Override
    public CommandExecutionHandler getExecutionHandler() {
        return this.commandExecutionHandler;
    }

    @Override
    public Application getApplication() {
        return this.application;
    }

    @Override
    public TranslationService getTranslationService() {
        return this.translationService;
    }
}
