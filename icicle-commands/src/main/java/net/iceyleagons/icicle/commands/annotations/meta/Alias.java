package net.iceyleagons.icicle.commands.annotations.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Nov. 1, 2021
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD, TYPE})
public @interface Alias {

    String[] value() default {};
}
