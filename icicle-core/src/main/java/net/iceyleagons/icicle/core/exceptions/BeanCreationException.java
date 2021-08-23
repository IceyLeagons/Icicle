package net.iceyleagons.icicle.core.exceptions;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

public class BeanCreationException extends Exception {

    public BeanCreationException(Constructor<?> constructor, String msg) {
        this(constructor, msg, null);
    }

    public BeanCreationException(Constructor<?> constructor, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + constructor.getDeclaringClass().getName() + ": " + msg, cause);
    }

    public BeanCreationException(Class<?> clazz, String msg) {
        this(clazz, msg, null);
    }

    public BeanCreationException(Class<?> clazz, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + clazz.getName() + ": " + msg, cause);
    }
}
