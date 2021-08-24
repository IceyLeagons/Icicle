package net.iceyleagons.icicle.core.proxy.handlers;

import net.iceyleagons.icicle.core.annotations.execution.Sync;
import net.iceyleagons.icicle.core.proxy.MethodAnnotationHandler;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class SyncMethodHandler extends MethodAnnotationHandler {

    public SyncMethodHandler() {
        super(Sync.class);
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        //TODO once Spigot API is present
        return proxy.invokeSuper(obj, args);
    }
}
