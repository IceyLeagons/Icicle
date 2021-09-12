package net.iceyleagons.icicle.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Targets marked with this annotation are meant for internal use only, therefore calling them outside the library
 * may cause significant issues.
 * <p>
 * Generally this annotation will not be used on every internal method, only on methods that can be accessed publicly.
 */
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.SOURCE)
public @interface Internal {
}
