package net.iceyleagons.icicle.core.proxy.interfaces;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;

public interface MethodDescriptor {

    ElementMatcher<? super MethodDescription> getMatcher();

}
