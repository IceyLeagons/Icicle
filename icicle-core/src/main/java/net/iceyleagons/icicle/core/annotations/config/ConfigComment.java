package net.iceyleagons.icicle.core.annotations.config;

import org.simpleyaml.configuration.comments.CommentType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
public @interface ConfigComment {

    String value();

    CommentType type() default CommentType.SIDE;

}
