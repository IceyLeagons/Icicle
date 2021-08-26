package net.iceyleagons.icicle.core.proxy;

import java.lang.reflect.Constructor;

public interface BeanProxyHandler {

    <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments);

}
