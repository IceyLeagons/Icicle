package net.iceyleagons.icicle.core.proxy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;


public class ByteBuddyProxyHandler implements BeanProxyHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ByteBuddyProxyHandler.class);
    private final Set<MethodInterceptor> methodInterceptor = new HashSet<>();

    @Override
    public <T> T createEnhancedBean(Constructor<T> constructor, Object[] arguments) throws BeanCreationException {
        ByteBuddy byteBuddy = new ByteBuddy();
        DynamicType.Builder<T> builder = byteBuddy.subclass(constructor.getDeclaringClass());

        for (MethodInterceptor interceptor : methodInterceptor) {
            LOGGER.debug("Registering method interceptor {} to builder. ", interceptor.getClass().getName());
            builder = builder.method(interceptor.getMethodDescripton()).intercept(interceptor.getImplementation());
        }

        try {
            LOGGER.debug("Creating enhanced proxy class.");
            return builder.make()
                    .load(constructor.getDeclaringClass().getClassLoader())
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
    public Set<MethodInterceptor> getInterceptors() {
        return this.methodInterceptor;
    }

    @Override
    public void registerInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor.add(methodInterceptor);
    }
}
