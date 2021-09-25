package net.iceyleagons.icicle.core.proxy.interfaces;

import net.bytebuddy.asm.Advice;
public interface MethodAdviceHandlerTemplate extends MethodDescriptor {

    Advice getAsmAdvice();

}
