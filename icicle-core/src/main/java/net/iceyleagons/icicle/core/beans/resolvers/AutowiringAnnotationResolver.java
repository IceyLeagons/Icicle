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
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

/**
 * An AutowiringAnnotationResolver is basically a registry for all {@link AutowiringAnnotationHandler}s.
 * This is what the core calls, and this is what routes the request to the appropriate handler.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @see AutowiringAnnotationHandler
 * @since Aug. 27, 2021
 */
public interface AutowiringAnnotationResolver {

    /**
     * Finds the appropriate {@link AutowiringAnnotationHandler} and calls it's {@link AutowiringAnnotationHandler#getValueForAnnotation(Annotation, Class)}
     *
     * @param annotationType the type of the annotation
     * @param annotation     the annotation
     * @param wantedType     the required type class (parameter's type)
     * @param <T>            the required type
     * @return the value or null
     * @see Nullable
     * @see AutowiringAnnotationHandler
     * @see AutowiringAnnotationHandler#getValueForAnnotation(Annotation, Class)
     */
    @Nullable <T> T getValueForAnnotation(Class<? extends Annotation> annotationType, Annotation annotation, Class<T> wantedType);

    /**
     * Registers a new {@link AutowiringAnnotationHandler}.
     *
     * @param handler the handler to register
     */
    void registerAutowiringAnnotationHandler(AutowiringAnnotationHandler handler);

    /**
     * Checks whether the supplied annotationType has an {@link AutowiringAnnotationHandler} linked to it,
     * aka. if it's registered.
     *
     * @param annotationType the annotation type
     * @return true if the annotation is registered
     */
    boolean has(Class<?> annotationType);
}
