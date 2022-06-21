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

package net.iceyleagons.icicle.core.beans;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.exceptions.MultipleInstanceException;
import net.iceyleagons.icicle.core.other.QualifierKey;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import org.jetbrains.annotations.Nullable;
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
@RequiredArgsConstructor
public class DelegatingBeanRegistry implements BeanRegistry {

    private static final Logger logger = LoggerFactory.getLogger(DelegatingBeanRegistry.class);

    private final Map<QualifierKey, Object> beans = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    private final Application application;

    // Global service providers can only supply one implementation for a service, therefore qualifiers don't need to be implemented.

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> Optional<T> getBean(Class<T> type) {
        return getBean(type, QualifierKey.DEFAULT_NAME);
    }

    @Override
    public <T> Optional<T> getBean(Class<T> type, String qualifier) {
        final GlobalServiceProvider globalServiceProvider = application.getGlobalServiceProvider();
        final QualifierKey key = new QualifierKey(type, qualifier);

        Optional<T> opt = beans.containsKey(key) ? Optional.ofNullable(ReflectionUtils.castIfNecessary(type, beans.get(key))) : Optional.empty();

        if (globalServiceProvider != null && opt.isEmpty() && globalServiceProvider.isRegistered(type)) {
            opt = globalServiceProvider.getService(type);
        }

        return opt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T getBeanNullable(Class<T> type) {
        return getBean(type).orElse(null);
    }

    @Override
    public <T> @Nullable T getBeanNullable(Class<T> type, String qualifier) {
        return getBean(type, qualifier).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRegistered(Class<?> type) {
        return isRegistered(type, QualifierKey.getQualifier(type));
    }

    @Override
    public boolean isRegistered(Class<?> type, String qualifier) {
        final GlobalServiceProvider globalServiceProvider = application.getGlobalServiceProvider();
        return this.beans.containsKey(new QualifierKey(type, qualifier)) || (globalServiceProvider != null && globalServiceProvider.isRegistered(type));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBean(Class<?> type, Object object) throws MultipleInstanceException {
        registerBean(type, object, QualifierKey.getQualifier(type));
    }

    @Override
    public void registerBean(Class<?> type, Object object, String qualifier) throws MultipleInstanceException {
        Asserts.isTrue(type != String.class && !type.isPrimitive(), "Strings and primitives cannot be registered as a bean!");

        if (!isRegistered(type, qualifier)) {
            beans.put(new QualifierKey(type, qualifier), object);
            // System.out.printf("Registered bean of type: %s. Qualifier: %s\n", type.getName(), qualifier);
            logger.debug("Registered bean of type: {}. Qualifier: {}", type.getName(), qualifier);
            return;
        }

        // logger.warn("Bean with type {} and qualifier {} already registered! Ignoring...", type.getName(), qualifier);
        throw new MultipleInstanceException(String.format("Bean (type: %s | qualifier: %s) already registered!", type.getName(), qualifier));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerBean(Object object) throws MultipleInstanceException {
        this.registerBean(object.getClass(), object);
    }

    @Override
    public void registerBean(Object object, String qualifier) throws MultipleInstanceException {
        this.registerBean(object.getClass(), object, qualifier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unregisterBean(Class<?> type) {
        unregisterBean(type, QualifierKey.getQualifier(type));
    }

    @Override
    public void unregisterBean(Class<?> type, String qualifier) {
        logger.debug("Unregistering bean of type {} and qualifier {}", type.getName(), qualifier);
        this.beans.remove(new QualifierKey(type, qualifier));
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
