package net.iceyleagons.icicle.core.proxy.interceptor.bean;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.Autowired;
import net.iceyleagons.icicle.core.annotations.Bean;
import net.iceyleagons.icicle.core.annotations.handlers.proxy.MethodInterceptionHandler;
import net.iceyleagons.icicle.core.beans.BeanRegistry;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 28, 2021
 */
@MethodInterceptionHandler
public class BeanHandler implements MethodInterceptorHandlerTemplate {

    private final BeanRegistry beanRegistry;

    @Autowired
    public BeanHandler(BeanRegistry beanRegistry) {
        this.beanRegistry = beanRegistry;
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMatcher() {
        return ElementMatchers.isAnnotatedWith(Bean.class);
    }

    @Override
    public Implementation getImplementation() {
        return MethodDelegation.to(new BeanDelegation(this.beanRegistry));
    }
}
