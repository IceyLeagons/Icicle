package net.iceyleagons.icicle.commands.middleware;

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
public @interface CommandMiddleware {

    /**
     * This value is here, so users can override our default middlewares.
     *
     * @return the middleware this implementation is meant to replace
     */
    Class<?> replaces() default Nothing.class;

    class Nothing { }
}
