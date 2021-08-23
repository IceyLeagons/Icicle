package net.iceyleagons.icicle.core.beans.resolvers;

import net.iceyleagons.icicle.core.beans.BeanRegistry;

import java.lang.reflect.Constructor;

public interface ConstructorParameterResolver {

    Object[] resolveConstructorParameters(Constructor<?> constructor, BeanRegistry beanRegistry);

}
