package net.iceyleagons.icicle.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 28, 2021
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Bean {
}
