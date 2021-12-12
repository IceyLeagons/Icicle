package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.annotations.handlers.CustomAutoCreateAnnotationHandler;

public interface CustomAutoCreateAnnotationResolver {

    void registerCustomAutoCreateAnnotationHandler(CustomAutoCreateAnnotationHandler handler);

    void onCreated(Object bean, Class<?> type) throws Exception;

}
