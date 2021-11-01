package net.iceyleagons.icicle.commands.annotations.params;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Nov. 1, 2021
 */
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface FlagOptional {

    String flag();
    String name();

}
