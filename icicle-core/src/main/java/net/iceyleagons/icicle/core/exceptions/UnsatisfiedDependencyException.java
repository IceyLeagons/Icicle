package net.iceyleagons.icicle.core.exceptions;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * This exception is used when a dependency can not be found when autowiring.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public class UnsatisfiedDependencyException extends Exception {

    /**
     * @param parameter the parameter of the constructor that caused the issue
     */
    public UnsatisfiedDependencyException(Parameter parameter) {
        super("Unsatisfied dependency expressed at parameter named " + parameter.getName() + ". Type: " + parameter.getType().getName());
    }

    /**
     * @param field the field of the class that caused the issue
     */
    public UnsatisfiedDependencyException(Field field) {
        super("Unsatisfied dependency expressed at field named " + field.getName() + ". Type: " + field.getType().getName());
    }
}
