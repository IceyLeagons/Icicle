package net.iceyleagons.icicle.core.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;
import net.iceyleagons.icicle.core.proxy.advices.MeasureAdvice;
import net.iceyleagons.icicle.core.annotations.execution.Measure;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;


public class ByteBuddyProxyHandler implements BeanProxyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBuddyProxyHandler.class);
    private final Set<MethodAdviceHandlerTemplate> adviceHandlers = new HashSet<>();

    static {
        ByteBuddyAgent.install();
    }

    @Override
    public <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments) throws BeanCreationException {
        ByteBuddy byteBuddy = new ByteBuddy();
        DynamicType.Builder<T> builder = byteBuddy
                .redefine(constructor.getDeclaringClass());

        for (MethodAdviceHandlerTemplate asmVisitorHandler : adviceHandlers) {
            LOGGER.debug("Registering method asm handler {} to builder. ", asmVisitorHandler.getClass().getName());
            builder = builder.visit(asmVisitorHandler.getAsmAdvice().on(asmVisitorHandler.getMatcher()));
        }

        //for (MethodInterceptor interceptor : methodInterceptor) {
        //    LOGGER.debug("Registering method interceptor {} to builder. ", interceptor.getClass().getName());
        //    builder = builder.method(interceptor.getMethodDescripton()).intercept(interceptor.getImplementation());
        //}
        builder = builder.visit(Advice.to(MeasureAdvice.class).on(ElementMatchers.isAnnotatedWith(Measure.class)));

        try {
            LOGGER.debug("Creating enhanced proxy class.");
            return builder.make()
                    .load(constructor.getDeclaringClass().getClassLoader(), ClassReloadingStrategy.fromInstalledAgent())
                    .getLoaded().getDeclaredConstructor(constructor.getParameterTypes()).newInstance(arguments);
        } catch (InvocationTargetException e) {
            throw new BeanCreationException(constructor, "Constructor execution resulted in an exception.", e);
        } catch (InstantiationException e) {
            throw new BeanCreationException(constructor, "Could not instantiate class. (Is it an abstract class?)");
        } catch (IllegalAccessException e) {
            throw new BeanCreationException(constructor, "Constructor is not accessible! (Is it accessible/public?)");
        } catch (NoSuchMethodException e) {
            throw new BeanCreationException(constructor, "Matching constructor in enhanced class can not be found!");
        }
    }

    @Override
    public Set<MethodAdviceHandlerTemplate> getMethodAdviceHandlers() {
        return this.adviceHandlers;
    }

    @Override
    public void registerInterceptor(MethodAdviceHandlerTemplate adviceHandler) {
        if (this.adviceHandlers.contains(adviceHandler)) return;
        this.adviceHandlers.add(adviceHandler);
    }
}
