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

package net.iceyleagons.icicle.core.beans.resolvers.impl;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.beans.resolvers.CustomAutoCreateAnnotationResolver;

import java.lang.annotation.Annotation;
import java.util.Map;

public class DelegatingCustomAutoCreateAnnotationResolver implements CustomAutoCreateAnnotationResolver {

    private final Map<Class<? extends Annotation>, CustomAutoCreateAnnotationHandler> handlers = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    @Override
    public void registerCustomAutoCreateAnnotationHandler(CustomAutoCreateAnnotationHandler handler) {
        for (Class<? extends Annotation> supportedAnnotation : handler.getSupportedAnnotations()) {
            handlers.put(supportedAnnotation, handler);
        }
    }

    @Override
    public void onCreated(Object bean, Class<?> type) throws Exception {
        for (Annotation annotation : type.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();

            if (handlers.containsKey(annotationType)) {
                handlers.get(annotationType).onCreated(bean, type);
            }
        }
    }

    @Override
    public boolean has(Class<?> type) {
        return handlers.containsKey(type);
    }
}
