package net.iceyleagons.icicle.core.exceptions;

import java.lang.reflect.Parameter;

public class UnsatisfiedDependencyException extends Exception {

    public UnsatisfiedDependencyException(Parameter parameter) {
        super("Unsatisfied dependency expressed at parameter named " + parameter.getName() + ". Type: " + parameter.getType().getName());
    }

}
