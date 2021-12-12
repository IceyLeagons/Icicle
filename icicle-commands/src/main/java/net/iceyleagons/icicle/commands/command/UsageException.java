package net.iceyleagons.icicle.commands.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.commands.annotations.meta.Usage;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@Getter
@RequiredArgsConstructor
public class UsageException {

    private final Usage usage;

}
