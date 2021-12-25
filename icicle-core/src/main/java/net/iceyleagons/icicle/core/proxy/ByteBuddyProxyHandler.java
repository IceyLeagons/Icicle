/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.core.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodAdviceHandlerTemplate;
import net.iceyleagons.icicle.core.proxy.interfaces.MethodInterceptorHandlerTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;


public class ByteBuddyProxyHandler implements BeanProxyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBuddyProxyHandler.class);

    static {
        System.out.println("[ByteBuddyProxyHandler] - Installing ByteBuddy Agent...");
        ByteBuddyAgent.install();

        new AgentBuilder.Default()
                .with(AgentBuilder.RedefinitionStrategy.RETRANSFORMATION)
                .with(AgentBuilder.InitializationStrategy.NoOp.INSTANCE)
                .with(AgentBuilder.TypeStrategy.Default.REBASE)
                .installOnByteBuddyAgent();
        System.out.println("[ByteBuddyProxyHandler] - Success!");
    }

    private final Set<MethodAdviceHandlerTemplate> adviceHandlers = new HashSet<>();
    private final Set<MethodInterceptorHandlerTemplate> interceptorHandlers = new HashSet<>();

    @Override
    public <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments) throws BeanCreationException {
        ByteBuddy byteBuddy = new ByteBuddy(); //.with(Implementation.Context.Disabled.Factory.INSTANCE);
        DynamicType.Builder<T> builder = byteBuddy
                .subclass(constructor.getDeclaringClass());

        for (MethodAdviceHandlerTemplate asmVisitorHandler : adviceHandlers) {
            LOGGER.debug("Registering method asm handler {} to builder. ", asmVisitorHandler.getClass().getName());
            builder = builder.visit(asmVisitorHandler.getAsmAdvice().on(asmVisitorHandler.getMatcher()));
        }

        for (MethodInterceptorHandlerTemplate interceptor : interceptorHandlers) {
            LOGGER.debug("Registering method interceptor handler {} to builder. ", interceptor.getClass().getName());
            builder = builder.method(interceptor.getMatcher()).intercept(interceptor.getImplementation());
        }

        //builder = builder.visit(Advice.to(MeasureAdvice.class).on(ElementMatchers.isAnnotatedWith(Measure.class)));

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
    public void registerAdviceTemplate(MethodAdviceHandlerTemplate adviceHandler) {
        if (this.adviceHandlers.contains(adviceHandler)) return;
        this.adviceHandlers.add(adviceHandler);
    }

    @Override
    public void registerInterceptorTemplate(MethodInterceptorHandlerTemplate interceptorTemplate) {
        if (this.interceptorHandlers.contains(interceptorTemplate)) return;
        this.interceptorHandlers.add(interceptorTemplate);
    }
}
