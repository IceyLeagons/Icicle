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

package net.iceyleagons.icicle.commands.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.annotations.meta.Description;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RegisteredCommand implements TabCompleter {

    private final RegisteredCommandManager manager;
    private final Class<?>[] paramTypes;
    private final String commandName;
    private final boolean suppliesTranslationKey;
    private final String[] aliases;

    private final Method method;
    private final Object origin;


    public String[] getAllCommandNames() {
        return ArrayUtils.appendToArray(this.aliases, this.commandName);
    }

    public Usage getUsage() {
        return method.isAnnotationPresent(Usage.class) ? method.getAnnotation(Usage.class) : null;
    }

    public Description getDescription() {
        return method.isAnnotationPresent(Description.class) ? method.getAnnotation(Description.class) : null;
    }

    public String execute(Object[] args) throws Exception {
        Object object = method.invoke(origin, args);
        return object == null ? null : object.toString();
    }

    public String getDefaultUsage(String rootCommand) {
        StringBuilder sb = new StringBuilder();

        sb.append("/").append(rootCommand).append(" ");
        sb.append(commandName);

        for (Parameter param : method.getParameters()) {
            Class<?> paramType = param.getType();
            if (paramType.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class))
                continue;
            sb.append(" ");

            boolean opt = paramType.isAnnotationPresent(Optional.class);
            sb.append(opt ? "[" : "<");
            sb.append(param.getName());
            sb.append(opt ? "]" : ">");
        }

        return sb.toString();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        int counter = args.length;

        // TODO figure out what to fix & why to fix
        Parameter param = method.getParameters()[counter]; // TODO fix
        if (param.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class)) {
            param = method.getParameters()[++counter];
        }

        CommandParameterResolverTemplate resolver = manager.getCommandService().getParamResolvers().get(param.getType());
        return resolver.onTabComplete(sender, command, alias, args);
    }
}
