package net.iceyleagons.icicle.core.configuration;

import net.iceyleagons.icicle.core.annotations.config.Property;
import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.AutowiringAnnotationHandler;
import net.iceyleagons.icicle.core.configuration.environment.ConfigurationEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Set;

@AnnotationHandler
public class ConfigPropertyAutowiringHandler implements AutowiringAnnotationHandler {

    private final ConfigurationEnvironment configurationEnvironment;

    public ConfigPropertyAutowiringHandler(ConfigurationEnvironment configurationEnvironment) {
        this.configurationEnvironment = configurationEnvironment;
    }

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(Property.class);
    }

    @Nullable
    @Override
    public <T> T getValueForAnnotation(Annotation annotation, Class<T> wantedType) {
        if (annotation instanceof Property) {
            return configurationEnvironment.getProperty(((Property) annotation).value(), wantedType).orElse(null);
        }

        return null;
    }
}
