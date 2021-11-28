package net.iceyleagons.icicle.core;


import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import net.iceyleagons.icicle.core.performance.PerformanceLog;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import net.iceyleagons.icicle.core.utils.ExecutionUtils;
import org.reflections.Reflections;

/**
 * Main class of Icicle.
 *
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Aug. 23, 2021
 */
public class Icicle {

    public static final String ICICLE_VERSION = "1.0.0";

    public static final boolean PERFORMANCE_DEBUG = true;

    public static final ClassLoader[] ICICLE_CLASS_LOADERS = new ClassLoader[]{Icicle.class.getClassLoader()};
    public static final Reflections ICICLE_REFLECTIONS = new Reflections("net.iceyleagons.icicle", ICICLE_CLASS_LOADERS);

    public static String getCopyrightText() {
        return "Icicle is licensed under the terms of MIT License.";
    }

    public static String getLoadText() {
        return String.format("Loading Icicle v%s. %s", ICICLE_VERSION, getCopyrightText());
    }

    public static void main(String[] args) throws BeanCreationException, UnsatisfiedDependencyException, CircularDependencyException {
        AbstractIcicleApplication abstractIcicleApplication = new AbstractIcicleApplication("net.iceyleagons.icicle.core") {
            @Override
            public ExecutionHandler getExecutionHandler() {
                return ExecutionUtils.debugHandler();
            }
        };

        abstractIcicleApplication.start();
        abstractIcicleApplication.getBeanManager().getBeanRegistry().getBean(Test.class).ifPresent(t -> t.test());
        abstractIcicleApplication.getBeanManager().getBeanRegistry().getBean(Test.Test2.class).ifPresent(t -> System.out.println(t.asd()));
        abstractIcicleApplication.getBeanManager().getBeanRegistry().getBean(Test.Test2.class).ifPresent(t -> System.out.println(t.asd()));

        System.out.println(PerformanceLog.dumpExecutionLog(abstractIcicleApplication));
    }
}
