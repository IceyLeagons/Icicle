/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.beans.handlers;

import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.Set;

/**
 * An AutowiringAnnotationHandler extends the default logic of AutoWiring by introducing annotation specific logic.
 * Classes that implement this interface, must annotate themselves with @{@link AnnotationHandler}
 * <p>
 * If a to-be-auto-wired parameter has an annotation, that is supported by this handler ({@link #getSupportedAnnotations()}),
 * the {@link net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver} will call this handler, to resolve that parameter.
 * Note that, parameters managed via this handler won't receive values from the regular life cycle, the handler must provide one (or null)
 * <p>
 * See an example at: {@link net.iceyleagons.icicle.core.configuration.ConfigPropertyAutowiringHandler}
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @see AnnotationHandler
 * @see net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver
 * @since Aug. 27, 2021
 */
public interface AutowiringAnnotationHandler {

    /**
     * @return the supported annotations, managed by this handler. Must not be null!
     * @see NotNull
     */
    @NotNull
    Set<Class<? extends Annotation>> getSupportedAnnotations();

    /**
     * Returns a value for the given annotation.
     *
     * @param annotation the annotation
     * @param wantedType the required type class (parameter's type)
     * @param <T>        the required type
     * @return the value or null
     * @see Nullable
     */
    @Nullable <T> T getValueForAnnotation(Annotation annotation, Class<T> wantedType, Parameter parameter);

}
