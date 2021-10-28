package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.annotations.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * A BeanRegistry is responsible for storing all the beans created during the application's runtime.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public interface BeanRegistry {

    /**
     * Returns the bean for the supplied class or an empty optional.
     *
     * @param type the class of the wanted bean
     * @param <T> type of the bean
     * @return the Optional containing the bean (if exists) or empty
     *
     * @see Optional
     */
    <T> Optional<T> getBean(Class<T> type);

    /**
     * Returns the bean for the supplied class or null.
     *
     * @param type the class of the wanted bean
     * @param <T> type of the bean
     * @return the bean (if exists) or null
     */
    @Nullable
    <T> T getBeanNullable(Class<T> type);

    /**
     * Checks whether a bean instance has been registered for the supplied class.
     *
     * @param type the class to check
     * @return true if a bean instance is registered for this class
     */
    boolean isRegistered(Class<?> type);

    /**
     * Registers a bean with the specified class (type).
     *
     * @param type the type to register the bean as
     * @param object the bean
     */
    void registerBean(Class<?> type, Object object);

    /**
     * Registers a bean using {@link Object#getClass()} as its type.
     *
     * @param object the bean
     */
    void registerBean(Object object);

    /**
     * Unregister a bean from the registry.
     * <p>
     * <b>WARNING!</b>
     * Should only be called internally, otherwise it may cause serious issues with dependency tree resolving.
     *
     * @param type the type to unregister
     * @see Internal
     */
    @Internal
    void unregisterBean(Class<?> type);


    /**
     * Cleans up the registry.
     * (Deletes every registered bean)
     *
     * <p>
     * <b>WARNING!</b>
     * Should only be called internally, and only when the application shuts down,
     * otherwise it may cause serious issues with dependency tree resolving.
     *
     * @see Internal
     */
    @Internal
    void cleanUp();

}
