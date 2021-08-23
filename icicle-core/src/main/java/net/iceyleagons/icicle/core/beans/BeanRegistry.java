package net.iceyleagons.icicle.core.beans;

import java.util.Optional;

public interface BeanRegistry {

    <T> Optional<T> getBean(Class<T> type);
    <T> T getBeanNullable(Class<T> type);

    boolean isRegistered(Class<?> type);

    void registerBean(Class<?> type, Object object);
    void registerBean(Object object);

}
