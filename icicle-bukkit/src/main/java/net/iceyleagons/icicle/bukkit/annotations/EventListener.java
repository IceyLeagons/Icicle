package net.iceyleagons.icicle.bukkit.annotations;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 17, 2021
 */
@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface EventListener {
}
