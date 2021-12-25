package net.iceyleagons.icicle.commands.annotations.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 12, 2021
 */
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface Description {

    String key();

    String defaultValue();


}
