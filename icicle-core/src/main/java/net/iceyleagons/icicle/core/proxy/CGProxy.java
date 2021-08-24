package net.iceyleagons.icicle.core.proxy;

import net.iceyleagons.icicle.core.proxy.handlers.AsyncMethodHandler;
import net.iceyleagons.icicle.core.proxy.handlers.SyncMethodHandler;
import net.sf.cglib.proxy.Enhancer;

public final class CGProxy {

    public static <T> T createEnhancedClass(Class<T> type) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(type);

        //TODO scan for these automatically so better dev experience
        enhancer.setCallback(MethodAnnotationHandler.asMethodInterceptor(
                new SyncMethodHandler(),
                new AsyncMethodHandler()
        ));

        //TODO with parameters and autowiring etc.
        return null;
    }

}
