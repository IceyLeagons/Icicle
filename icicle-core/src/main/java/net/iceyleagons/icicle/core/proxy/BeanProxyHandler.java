package net.iceyleagons.icicle.core.proxy;

import net.iceyleagons.icicle.core.exceptions.BeanCreationException;

import java.lang.reflect.Constructor;
import java.util.Set;

public interface BeanProxyHandler {

    <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments) throws BeanCreationException;

    Set<MethodInterceptor> getInterceptors();

    void registerInterceptor(MethodInterceptor methodInterceptor);
}
