package net.iceyleagons.icicle.core.beans.resolvers.impl;

import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.beans.resolvers.ConstructorParameterResolver;

import java.lang.reflect.Constructor;

public class DelegatingConstructorParameterResolver implements ConstructorParameterResolver {

    @Override
    public Object[] resolveConstructorParameters(Constructor<?> constructor, BeanRegistry beanRegistry) {
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];

        if (paramTypes.length == 0) return params;

        for (int i = 0; i < paramTypes.length; i++) {
            params[i] = beanRegistry.getBeanNullable(paramTypes[i]);
        }

        return params;
    }
}
