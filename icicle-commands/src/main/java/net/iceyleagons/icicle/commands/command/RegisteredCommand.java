package net.iceyleagons.icicle.commands.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.iceyleagons.icicle.commands.annotations.meta.Description;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;
import net.iceyleagons.icicle.commands.manager.RegisteredCommandManager;
import net.iceyleagons.icicle.core.annotations.Internal;
import net.iceyleagons.icicle.utilities.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RegisteredCommand {

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
}
