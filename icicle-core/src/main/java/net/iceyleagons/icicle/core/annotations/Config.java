package net.iceyleagons.icicle.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.ElementType.TYPE;

@AutoCreate
@Target(TYPE)
@Retention(RUNTIME)
public @interface Config {

    /**
     * @return the path to the config yaml file, relative from the plugin datafolder
     */
    String value();

}
