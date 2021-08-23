package net.iceyleagons.icicle.exceptions;

import java.lang.reflect.Field;

public class InjectionException extends Exception {

    public InjectionException(Field field) {
        super(String.format("Field found in %s, named %s cannot be injected!", field.getDeclaringClass().getName(), field.getName()));
    }
}
