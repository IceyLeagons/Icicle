package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.resolvers.AutowiringAnnotationResolver;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

public class DelegatingConstructorParameterResolver implements ConstructorParameterResolver {

    private final AutowiringAnnotationResolver autowiringAnnotationResolver;

    public DelegatingConstructorParameterResolver(AutowiringAnnotationResolver autowiringAnnotationResolver) {
        this.autowiringAnnotationResolver = autowiringAnnotationResolver;
    }

    @Override
    public Object[] resolveConstructorParameters(Constructor<?> constructor, BeanRegistry beanRegistry) {
        Parameter[] parameters = constructor.getParameters();
        Object[] params = new Object[parameters.length];

        if (parameters.length == 0) return params;

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class<?> type = parameter.getType();

            Object result = beanRegistry.getBeanNullable(type);

            if (parameter.getAnnotations().length != 0 && result == null) {
                for (Annotation annotation : parameter.getAnnotations()) {
                    result = autowiringAnnotationResolver.getValueForAnnotation(annotation.annotationType(), annotation, type);
                    if (result != null) break;
                }    
            }
            
            params[i] = result;
        }

        return params;
    }
}
