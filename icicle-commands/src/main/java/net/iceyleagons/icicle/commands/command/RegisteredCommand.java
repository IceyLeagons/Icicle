package net.iceyleagons.icicle.commands.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.iceyleagons.icicle.commands.annotations.meta.Description;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;
import net.iceyleagons.icicle.commands.annotations.params.FlagOptional;
import net.iceyleagons.icicle.commands.annotations.params.Optional;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.commands.params.CommandParameterResolverTemplate;
import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
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
        return method.invoke(origin, args).toString();
    }

    public String getDefaultUsage() {
        StringBuilder sb = new StringBuilder();

        sb.append(commandName);

        for (Parameter param : method.getParameters()) {
            Class<?> paramType = param.getType();
            if (paramType.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class)) continue;
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

        Parameter param = method.getParameters()[counter]; // TODO fix
        if (param.isAnnotationPresent(net.iceyleagons.icicle.commands.annotations.params.CommandSender.class)) {
            param = method.getParameters()[++counter];
        }

        CommandParameterResolverTemplate resolver = manager.getCommandService().getParamResolvers().get(param.getType());

        return resolver.onTabComplete(sender, command, alias, args);
    }
}
