package net.iceyleagons.icicle.commands.manager;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.ArrayUtils;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class RegisteredCommand {

    private final Class<?>[] paramTypes;
    private final String commandName;
    private final String[] aliases;

    public String[] getAllCommandNames() {
        return ArrayUtils.appendToArray(this.aliases, this.commandName);
    }
}
