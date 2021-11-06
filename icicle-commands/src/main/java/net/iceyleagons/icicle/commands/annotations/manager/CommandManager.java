package net.iceyleagons.icicle.commands.annotations.manager;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Nov. 1, 2021
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface CommandManager {

    String value();

    String description() default "";

    /**
     * This requires the translation system!
     * If left empty, the system will use the default (hard-coded) message.
     *
     * @return the translation key to the permission error text.
     */
    String permissionError() default "";

    /**
     * This requires the translation system!
     * If left empty, the system will use the default (hard-coded) message.
     *
     * @return the translation key to the player-only error text.
     */
    String playerOnly() default "";
}
