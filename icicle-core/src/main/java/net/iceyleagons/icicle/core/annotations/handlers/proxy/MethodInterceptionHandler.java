package net.iceyleagons.icicle.core.annotations.handlers.proxy;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 */
public @interface MethodInterceptionHandler { }
