package net.iceyleagons.icicle.core.annotations.handlers;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface CustomAutoCreateAnnotationHandler {

    @NotNull
    Set<Class<? extends Annotation>> getSupportedAnnotations();

    void onCreated(Object bean, Class<?> type) throws Exception;

}
