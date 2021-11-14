package net.iceyleagons.icicle.core.proxy.advices;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.annotations.execution.Measure;
import net.iceyleagons.icicle.core.annotations.handlers.proxy.MethodAdviceHandler;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;

@MethodAdviceHandler
public class MeasureAdviceHandler implements MethodAdviceHandlerTemplate {

    @Override
    public Advice getAsmAdvice() {
        return Advice.to(MeasureAdvice.class);
    }

    @Override
    public ElementMatcher<? super MethodDescription> getMatcher() {
        return ElementMatchers.isAnnotatedWith(Measure.class);
    }
}
