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

package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;

/**
 * A CustomAutoCreateAnnotationResolver is basically a registry for all {@link CustomAutoCreateAnnotationHandler}s.
 * This is what the core calls, and this is what routes the request to the appropriate handler.
 *
 * @version 1.0.0
 * @author TOTHTOMI
 * @since Aug. 28, 2021
 * @see AutowiringAnnotationHandler
 */
public interface CustomAutoCreateAnnotationResolver {

    /**
     * Registers a new {@link CustomAutoCreateAnnotationHandler}.
     *
     * @param handler the handler to register
     */
    void registerCustomAutoCreateAnnotationHandler(CustomAutoCreateAnnotationHandler handler);

    /**
     * This method is called after the bean has been initialized and auto-wired.
     * This resolver will route this call to the appropriate {@link CustomAutoCreateAnnotationHandler#onCreated(Object, Class)}
     *
     * The supplied type must be used instead of calling {@link #getClass()} on the bean, due to proxying.
     * The type is the actual type of the bean, while the bean's class can be proxied.
     *
     * @param bean the bean
     * @param type the actual type of the bean. (supplied due to proxying)
     * @throws Exception if something is not right for the handler (ex. bean does not implement an interface)
     */
    void onCreated(Object bean, Class<?> type) throws Exception;

    /**
     * Checks whether the supplied annotationType has an {@link CustomAutoCreateAnnotationHandler} linked to it,
     * aka. if it's registered.
     *
     * @param annotationType the annotation type
     * @return true if the annotation is registered
     */
    boolean has(Class<?> annotationType);
}
