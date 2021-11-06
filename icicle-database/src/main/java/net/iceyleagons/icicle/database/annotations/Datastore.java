package net.iceyleagons.icicle.database.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface Datastore {

    String value(); //Name of datastore (SQL - tablename, MONGO - collection name)

}
