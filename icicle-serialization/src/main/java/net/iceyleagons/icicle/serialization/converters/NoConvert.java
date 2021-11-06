package net.iceyleagons.icicle.serialization.converters;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * This annotation exists so we can disable DefaultConverters.
 * When using @{@link Convert} the default one will automatically be replaced with the one specified by the annotation.
 * And when not using {@link Convert} and there are no DefaultConverters for that type we don't convert to begin with.
 * <p>
 * So the sole purpose of this annotation is to disable DefaultConverters in cases where
 * we just don't want the "simpler form" of the type.
 *
 * @author TOTHTOMI
 * @since Oct. 26, 2021
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface NoConvert {
}
