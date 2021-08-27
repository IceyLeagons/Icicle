package net.iceyleagons.icicle.core;


import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.annotations.config.ConfigField;
import net.iceyleagons.icicle.core.annotations.config.Property;
import org.reflections.Reflections;

/**
 * Main class of Icicle.
 *
 * @version 1.1.0
 * @since Aug. 23, 2021
 * @author TOTHTOMI
 */
public class Icicle {

    public static final String ICICLE_VERSION = "0.1.0";

    public static final ClassLoader[] ICICLE_CLASS_LOADERS = new ClassLoader[]{ Icicle.class.getClassLoader() };
    public static final Reflections ICICLE_REFLECTIONS = new Reflections("net.iceyleagons.icicle", ICICLE_CLASS_LOADERS);

    public static void main(String[] args) {
        Application application = new IcicleApplication("net.iceyleagons.icicle");
        try {
            application.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Service
    static class Test {

        public Test(@Property("asd.test") String test) {
            System.out.println("Got: " + test);
        }
    }
}
