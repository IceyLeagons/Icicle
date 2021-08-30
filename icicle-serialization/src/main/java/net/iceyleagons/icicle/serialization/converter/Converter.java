package net.iceyleagons.icicle.serialization.converter;

import net.iceyleagons.icicle.core.annotations.AutoCreate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@AutoCreate
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter { }
