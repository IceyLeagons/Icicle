package net.iceyleagons.icicle.core.proxy.interceptor.bean;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.iceyleagons.icicle.core.beans.BeanRegistry;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 28, 2021
 */
@RequiredArgsConstructor
public class BeanDelegation {

    private final BeanRegistry beanRegistry;

    @RuntimeType
    public Object run(@SuperCall Callable<?> callable, @Origin Method method) throws Exception {
        Class<?> beanType = method.getReturnType();
        if (beanType.equals(Void.class)) {
            throw new IllegalStateException("Method marked with @Bean must have a non-void return type.");
        }

        if (this.beanRegistry.isRegistered(beanType)) {
            return this.beanRegistry.getBeanNullable(beanType);
        }

        Object bean = callable.call();
        this.beanRegistry.registerBean(beanType, bean);
        return bean;
    }
}
