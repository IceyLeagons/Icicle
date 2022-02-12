/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.core;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 26, 2021
 */
@Experimental
public class GlobalBeanRegistry implements BeanRegistry {

    public static final GlobalBeanRegistry INSTANCE = new GlobalBeanRegistry();
    private static final Logger logger = LoggerFactory.getLogger(GlobalBeanRegistry.class);
    private static final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();
    private static boolean registered = false;

    public GlobalBeanRegistry() {
        if (registered) {
            throw new UnsupportedOperationException("An instance of GlobalBeanRegistry has already been created!");
        }
        registered = true;
    }

    public static void registerService(Class<?> service, Object provider) {
        INSTANCE.registerBean(service, provider);
    }

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
        return beans.containsKey(type) ? ReflectionUtils.castIfNecessary(type, beans.get(type)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRegistered(Class<?> type) {
        return beans.containsKey(type);
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
        beans.remove(type);
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
