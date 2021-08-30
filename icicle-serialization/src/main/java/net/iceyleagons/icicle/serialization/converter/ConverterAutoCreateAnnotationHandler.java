package net.iceyleagons.icicle.serialization.converter;

import net.iceyleagons.icicle.core.annotations.handlers.AnnotationHandler;
import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

@AnnotationHandler
public class ConverterAutoCreateAnnotationHandler implements CustomAutoCreateAnnotationHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterAutoCreateAnnotationHandler.class);
    public static final Map<Type, DataSerializationConverter<?,?>> converters = new HashMap<>();

    @Override
    public @NotNull Set<Class<? extends Annotation>> getSupportedAnnotations() {
        return Collections.singleton(Converter.class);
    }

    @Override
    public void onCreated(Object bean, Class<?> type) {
        if (!(bean instanceof DataSerializationConverter)) {
            LOGGER.warn("Type {} is marked with @Converter but does not implement DataSerializationConverter interface!", type.getName());
            return;
        }

        //BEWARE some ugly code ahead, and this is all because of stupid generic stuff
        for (Type genericInterface : bean.getClass().getGenericInterfaces()) {
            System.out.println(genericInterface.getTypeName());
            if (genericInterface.getTypeName().contains(DataSerializationConverter.class.getTypeName())) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();
                converters.put(genericTypes[0], (DataSerializationConverter<?, ?>) bean);
            }
        }

    }
}
