package net.iceyleagons.icicle.core.annotations.handlers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface AutowiringAnnotationHandler {

    @NotNull
    Set<Class<? extends Annotation>> getSupportedAnnotations();

    @Nullable <T> T getValueForAnnotation(Annotation annotation, Class<T> wantedType);

}
