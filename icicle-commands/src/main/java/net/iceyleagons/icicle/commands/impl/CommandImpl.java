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

import lombok.Getter;
import net.iceyleagons.icicle.commands.Command;
import net.iceyleagons.icicle.commands.CommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.commands.params.annotations.CommandSender;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@Getter
public class CommandImpl implements Command {

    private final CommandManager manager;
    private final String name;
    private final boolean supplyingTranslationKeys;

    private final Method method;
    private final Object origin;

    private final String[] aliases;

    public CommandImpl(CommandManager manager, String name, boolean returnsTranslationKey, Method method, Object origin) {
        this.manager = manager;
        this.name = name;
        this.supplyingTranslationKeys = returnsTranslationKey;

        this.method = method;
        this.origin = origin;
        this.aliases = Command.getAliases(method);
    }

    @Override
    public List<String> getOptions(String[] args) {
        final Parameter[] params = method.getParameters();
        final int index = args.length;

        if (index >= params.length) {
            return Collections.emptyList();
        }

        Parameter param = params[index];
        if (param.isAnnotationPresent(CommandSender.class)) {
            param = params[index + 1];
        }

        CommandParameterResolverTemplate resolver = manager.getCommandService().getParameterResolverStore().get(param.getType());
        return resolver.getOptions(args);
    }
}
