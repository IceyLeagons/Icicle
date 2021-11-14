package net.iceyleagons.icicle.core.proxy.interfaces;

import net.bytebuddy.implementation.Implementation;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 13, 2021
 */
public interface MethodInterceptorHandlerTemplate extends MethodDescriptor {

    Implementation getImplementation();
}
