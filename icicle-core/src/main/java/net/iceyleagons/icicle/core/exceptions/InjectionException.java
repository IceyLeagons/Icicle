package net.iceyleagons.icicle.core.exceptions;

import java.lang.reflect.Field;

/**
 * This exception is used when a field cannot be injected due to other exceptions.
 *
 * @version 1.0.0
 * @since Aug. 23, 2021
 * @author TOTHTOMI
 */
public class InjectionException extends Exception {

    /**
     * @param field the field of the class that caused the issue
     */
    public InjectionException(Field field) {
        super(String.format("Field found in %s, named %s cannot be injected!", field.getDeclaringClass().getName(), field.getName()));
    }
}
