package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DelegatingBeanRegistry implements BeanRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingBeanRegistry.class);
    private final Map<Class<?>, Object> beans = new HashMap<>();

    @Override
    public <T> Optional<T> getBean(Class<T> type) {
        return beans.containsKey(type) ? Optional.ofNullable(BeanUtils.castIfNecessary(type, beans.get(type))) : Optional.empty();
    }

    @Override
    public <T> T getBeanNullable(Class<T> type) {
        return BeanUtils.castIfNecessary(type, this.beans.get(type));
    }

    @Override
    public boolean isRegistered(Class<?> type) {
        return beans.containsKey(type);
    }

    @Override
    public void registerBean(Class<?> type, Object object) {
        if (!isRegistered(type)) {
            beans.put(type, object);
            logger.info("Registered bean of type: {}", type.getName());
            return;
        }

        logger.warn("Bean with type {} already registered! Ignoring...", type.getName());
    }

    @Override
    public void registerBean(Object object) {
        this.registerBean(object.getClass(), object);
    }

    @Override
    public void unregisterBean(Class<?> type) {
        logger.debug("Unregistering bean of type {}", type.getName());
        this.beans.remove(type);
    }
}
