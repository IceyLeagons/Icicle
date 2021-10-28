package net.iceyleagons.icicle.core.beans;

import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of {@link BeanRegistry}.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public class DelegatingBeanRegistry implements BeanRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingBeanRegistry.class);
    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> getBean(Class<T> type) {
        return beans.containsKey(type) ? Optional.ofNullable(ReflectionUtils.castIfNecessary(type, beans.get(type))) : Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBeanNullable(Class<T> type) {
        return this.beans.containsKey(type) ? ReflectionUtils.castIfNecessary(type, this.beans.get(type)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRegistered(Class<?> type) {
        return this.beans.containsKey(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBean(Class<?> type, Object object) {
        Asserts.isTrue(type != String.class && !type.isPrimitive(), "Strings and primitives cannot be registered as a bean!");

        if (!isRegistered(type)) {
            beans.put(type, object);
            logger.info("Registered bean of type: {}", type.getName());
            return;
        }

        logger.warn("Bean with type {} already registered! Ignoring...", type.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBean(Object object) {
        this.registerBean(object.getClass(), object);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterBean(Class<?> type) {
        logger.debug("Unregistering bean of type {}", type.getName());
        this.beans.remove(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cleanUp() {
        logger.info("Cleaning up...");
        beans.clear();
    }
}
