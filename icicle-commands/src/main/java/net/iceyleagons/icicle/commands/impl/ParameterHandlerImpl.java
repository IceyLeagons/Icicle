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

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.Command;
import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.commands.CommandService;
import net.iceyleagons.icicle.commands.ParameterHandler;
import net.iceyleagons.icicle.commands.exception.ParamParsingException;
import net.iceyleagons.icicle.commands.exception.ParameterValidationException;
import net.iceyleagons.icicle.commands.params.annotations.CommandSender;
import net.iceyleagons.icicle.commands.params.annotations.Concat;
import net.iceyleagons.icicle.commands.params.annotations.FlagOptional;
import net.iceyleagons.icicle.commands.params.annotations.Optional;
import net.iceyleagons.icicle.commands.utils.ArgUtils;
import net.iceyleagons.icicle.utilities.Defaults;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Parameter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@RequiredArgsConstructor
public class ParameterHandlerImpl implements ParameterHandler {

    private final CommandManager commandManager;
    private final CommandService commandService;

    @Override
    public Object[] getParameters(Command command, String[] args, Object sender) throws ParameterValidationException, ParamParsingException {
        final Parameter[] params = command.getMethod().getParameters();
        final Object[] result = new Object[params.length];

        int argCounter = 0;
        String wholeArgs = ArgUtils.join(args, 0);

        for (int i = 0; i < params.length; i++) {
            final Parameter param = params[i];

            if (param.isAnnotationPresent(CommandSender.class)) {
                result[i] = ReflectionUtils.castIfNecessary(param.getType(), sender);
                continue;
            }

            if (java.util.Optional.class.isAssignableFrom(param.getType())) {
                if (argCounter < args.length) {
                    result[i] = java.util.Optional.ofNullable(this.commandService.resolveParameter(param.getType(), param, this.commandManager, args[argCounter++], sender));
                    continue;
                }

                result[i] = java.util.Optional.ofNullable(Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null));
                continue;
            }

            if (param.isAnnotationPresent(Optional.class)) {
                if (argCounter < args.length) {
                    result[i] = this.commandService.resolveParameter(param.getType(), param, this.commandManager, args[argCounter++], sender);
                    continue;
                }

                result[i] = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
                continue;
            }

            if (param.isAnnotationPresent(FlagOptional.class)) {
                result[i] = handleFlagOptional(wholeArgs, sender, param);
                continue;
            }

            if (param.isAnnotationPresent(Concat.class)) {
                result[i] = handleConcat(argCounter, args, param);
                break;
            }

            if (param.getType().isArray()) {
                result[i] = handleArray(argCounter, sender, args, param);
                break;
            }

            // In any other cases, while we still have string arguments:
            if (argCounter < args.length) {
                result[i] = this.commandService.resolveParameter(param.getType(), param, this.commandManager, args[argCounter++], sender);
                continue;
            }

            // In any other cases, if we have run out of string arguments
            // TODO too few arguments exception
        }

        return result;
    }

    private Object handleFlagOptional(String wholeArgs, Object sender, Parameter param) throws ParameterValidationException, ParamParsingException {
        FlagOptional flagOptional = param.getAnnotation(FlagOptional.class);
        String flag = flagOptional.value();
        Matcher matcher = Pattern.compile("-" + flag + "(.*?(?= -| $|$))").matcher(wholeArgs);
        if (matcher.find()) {
            String value = matcher.group(1).stripIndent();
            if (value == null) {
                return Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
            }

            return this.commandService.resolveParameter(param.getType(), param, this.commandManager, value, sender);
        }

        return Defaults.DEFAULT_TYPE_VALUES.getOrDefault(param.getType(), null);
    }

    private Object handleArray(int argCounter, Object sender, String[] args, Parameter param) throws ParameterValidationException, ParamParsingException {
        final Class<?> compType = param.getType().getComponentType();

        Object array = GenericUtils.createGenericArrayWithoutCasting(compType, args.length - argCounter);
        for (int j = argCounter; j < args.length; j++) {
            Array.set(array, j - argCounter, commandService.resolveParameter(compType, param, this.commandManager, args[j], sender));
        }

        return array;
    }

    private String handleConcat(int argCounter, String[] args, Parameter parameter) {
        String[] array = new String[args.length - argCounter];
        if (args.length - argCounter >= 0) {
            System.arraycopy(args, argCounter, array, 0, args.length - argCounter);
        }

        return String.join(" ", array);
    }
}
