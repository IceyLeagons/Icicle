package net.iceyleagons.icicle.core.annotations.handlers;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface MethodAdviceHandler {
}
