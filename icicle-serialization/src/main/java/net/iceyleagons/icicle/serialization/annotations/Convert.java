package net.iceyleagons.icicle.serialization.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface Convert {

    /**
     * @return the class of the converter
     */
    Class<?> value();

}
