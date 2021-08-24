package net.iceyleagons.icicle.core.proxy;

import lombok.Getter;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.annotation.Annotation;

@Getter
public abstract class MethodAnnotationHandler implements MethodInterceptor {

    private final Class<? extends Annotation> annotation;

    public MethodAnnotationHandler(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public static MethodInterceptor asMethodInterceptor(MethodAnnotationHandler... handlers) {
        return (obj, method, args, proxy) -> {
            for (MethodAnnotationHandler handler : handlers) {
                if (method.isAnnotationPresent(handler.getAnnotation())) {
                    return handler.intercept(obj, method, args, proxy);
                }
            }

            return proxy.invokeSuper(obj, args);
        };
    }
}
