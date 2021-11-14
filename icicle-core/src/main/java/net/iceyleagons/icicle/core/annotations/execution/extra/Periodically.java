package net.iceyleagons.icicle.core.annotations.execution.extra;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 14, 2021
 */
@Target(METHOD)
@Retention(RUNTIME)
public @interface Periodically {

    long period();
    TimeUnit unit();

}
