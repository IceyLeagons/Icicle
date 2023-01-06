/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

import net.iceyleagons.icicle.core.Application;

import java.util.Optional;

/**
 * Similar to {@link BeanRegistry}, but reserved for {@link net.iceyleagons.icicle.core.annotations.service.GlobalService}s.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 25, 2022
 */
public interface GlobalServiceProvider {

    /**
     * Returns the service described by the type supplied (or an empty {@link Optional})
     *
     * @param type the service type class
     * @param <T>  the service type
     * @return the {@link Optional}nal containing the service or empty
     */
    <T> Optional<T> getService(Class<T> type);

    /**
     * Used for checking whether a service is registered into this provider.
     *
     * @param type the service type class
     * @return true if it is registered, false otherwise
     */
    boolean isRegistered(Class<?> type);

    /**
     * Used for registering a service into the provider.
     *
     * @param interfaceType the interfaceType
     * @param providerType  the provider (implementation) type
     * @param object        the provider bean
     * @param registrar     the application who registered it
     * @throws Exception if something goes wrong
     */
    void registerService(Class<?> interfaceType, Class<?> providerType, Object object, Application registrar) throws Exception;
}