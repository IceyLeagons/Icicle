package net.iceyleagons.icicle.nms.utils;

import org.bukkit.Bukkit;

import java.util.logging.Logger;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
public class ClassHelper {

    private static final String cbString;
    private static final String mcString;
    private static final Logger logger = Logger.getLogger("ClassHelper");

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        cbString = "org.bukkit.craftbukkit." + version + ".";
        mcString = "net.minecraft.server." + version + ".";
    }

    public static AdvancedClass<?> getBukkitClass(String name) {
        return new AdvancedClass<>(getClass(cbString + name));
    }

    public static AdvancedClass<?> getNMSClass(String name) {
        return new AdvancedClass<>(getClass(mcString + name));
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            logger.warning("Class " + name + " was not found due to: " + e.getMessage());
            return null;
        }
    }
}
