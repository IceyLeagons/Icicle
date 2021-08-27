package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;

public interface AutowiringAnnotationResolver {

    @Nullable
    <T> T getValueForAnnotation(Class<? extends Annotation> annotationType, Annotation annotation, Class<T> wantedType);
    void registerAutowiringAnnotationHandler(AutowiringAnnotationHandler handler);

}
