package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.annotations.Internal;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface BeanRegistry {

    <T> Optional<T> getBean(Class<T> type);

    @Nullable
    <T> T getBeanNullable(Class<T> type);

    boolean isRegistered(Class<?> type);

    void registerBean(Class<?> type, Object object);

    void registerBean(Object object);

    boolean contains(Class<?> type);

    @Internal
    void unregisterBean(Class<?> type);

    @Internal
    void cleanUp();

}
