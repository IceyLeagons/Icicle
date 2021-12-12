package net.iceyleagons.icicle.commands.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@Getter
@RequiredArgsConstructor
public class CommandNotFoundException extends Exception {

    private final String command;

}
