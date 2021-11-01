package net.iceyleagons.icicle.commands.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Nov. 1, 2021
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GetterCommand {

    /**
     * Available translation parameters:
     * {var} - the name of the variable
     * {state} - the current state of the variable
     *
     * @return the message to be printed out when successful.
     */
    String value();

}
