package net.iceyleagons.icicle.commands.annotations.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @since Nov. 1, 2021
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Usage {

    String key();

    String defaultValue();

}
