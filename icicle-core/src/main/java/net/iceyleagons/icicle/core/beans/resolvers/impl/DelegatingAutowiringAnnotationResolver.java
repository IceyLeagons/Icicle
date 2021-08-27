package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DelegatingAutowiringAnnotationResolver implements AutowiringAnnotationResolver {

    private final Map<Class<? extends Annotation>, AutowiringAnnotationHandler> handlers = new ConcurrentHashMap<>();

    @Override
    public void registerAutowiringAnnotationHandler(AutowiringAnnotationHandler handler) {
        for (Class<? extends Annotation> supportedAnnotation : handler.getSupportedAnnotations()) {
            handlers.put(supportedAnnotation, handler);
        }
    }

    @Override
    public <T> T getValueForAnnotation(Class<? extends Annotation> annotationType, Annotation annotation, Class<T> wantedType) {
        return handlers.containsKey(annotationType) ? handlers.get(annotationType).getValueForAnnotation(annotation, wantedType) : null;
    }
}
