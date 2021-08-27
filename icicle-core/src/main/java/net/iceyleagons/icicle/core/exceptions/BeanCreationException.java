package net.iceyleagons.icicle.core.exceptions;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;

/**
 * This exception is used when a bean cannot be created due to other exceptions/errors.
 *
 * @version 1.0.0
 * @since Aug. 23, 2021
 * @author TOTHTOMI
 */
public class BeanCreationException extends Exception {

    /**
     * @param constructor the constructor that was used when attempted to create the bean
     * @param msg the error message
     */
    public BeanCreationException(Constructor<?> constructor, String msg) {
        this(constructor, msg, null);
    }

    /**
     * @param constructor the constructor that was used when attempted to create the bean
     * @param msg the error message
     * @param cause the underlying exception, that caused this exception
     */
    public BeanCreationException(Constructor<?> constructor, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + constructor.getDeclaringClass().getName() + ": " + msg, cause);
    }

    /**
     * @param clazz the clazz that was used when attempted to create the bean
     * @param msg the error message
     */
    public BeanCreationException(Class<?> clazz, String msg) {
        this(clazz, msg, null);
    }

    /**
     * @param clazz the clazz that was used when attempted to create the bean
     * @param msg the error message
     * @param cause the underlying exception, that caused this exception
     */
    public BeanCreationException(Class<?> clazz, String msg, @Nullable Throwable cause) {
        super("Failed to create bean named " + clazz.getName() + ": " + msg, cause);
    }
}
