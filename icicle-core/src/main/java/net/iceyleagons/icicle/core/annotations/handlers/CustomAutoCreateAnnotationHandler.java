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

package net.iceyleagons.icicle.core.annotations.handlers;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * CustomAutoCreateAnnotationHandlers are used to write custom logic to new {@link net.iceyleagons.icicle.core.annotations.AutoCreate} subtypes.
 * After the AutoCreate subtype has been initialized by the {@link net.iceyleagons.icicle.core.beans.BeanManager} it will call this handler, to handle the
 * additional logic required for that type(s).
 * <p>
 * Note, that all custom annotations must annotate the @{@link net.iceyleagons.icicle.core.annotations.AutoCreate} annotation for this to work!
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 28, 2021
 */
public interface CustomAutoCreateAnnotationHandler {

    /**
     * @return the supported annotations, managed by this handler. Must not be null!
     * @see NotNull
     */
    @NotNull
    Set<Class<? extends Annotation>> getSupportedAnnotations();

    /**
     * This method is called after the bean has been initialized and auto-wired.
     * <p>
     * The supplied type must be used instead of calling {@link #getClass()} on the bean, due to proxying.
     * The type is the actual type of the bean, while the bean's class can be proxied.
     *
     * @param bean the bean
     * @param type the actual type of the bean. (supplied due to proxying)
     * @throws Exception if something is not right for the handler (ex. bean does not implement an interface)
     */
    void onCreated(Object bean, Class<?> type) throws Exception;

}
