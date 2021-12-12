package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.core.beans.resolvers.CustomAutoCreateAnnotationResolver;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelegatingCustomAutoCreateAnnotationResolver implements CustomAutoCreateAnnotationResolver {

    private final Map<Class<? extends Annotation>, CustomAutoCreateAnnotationHandler> handlers = new ConcurrentHashMap<>();

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
}
