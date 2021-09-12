package net.iceyleagons.icicle.core.proxy;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.matcher.ElementMatcher;


public interface MethodInterceptor {

    ElementMatcher<? super MethodDescription> getMethodDescripton();

    Implementation getImplementation();

}
