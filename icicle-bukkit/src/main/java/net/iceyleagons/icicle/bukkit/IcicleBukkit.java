package net.iceyleagons.icicle.bukkit;

import net.iceyleagons.icicle.bukkit.listeners.PluginStatusListener;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.annotations.IcicleApplication;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.exceptions.CircularDependencyException;
import net.iceyleagons.icicle.core.exceptions.UnsatisfiedDependencyException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class IcicleBukkit extends JavaPlugin {

    public static final Map<JavaPlugin, BukkitApplication> RUNNING_APPLICATIONS = new HashMap<>();

    public static void startNewApplication(JavaPlugin javaPlugin) {
        try {
            Class<?> clazz = Class.forName(javaPlugin.getDescription().getMain());

            if (clazz.isAnnotationPresent(IcicleApplication.class)) {
                String mainPackage = clazz.getAnnotation(IcicleApplication.class).value();

                BukkitApplication bukkitApplication = new BukkitApplication(mainPackage, javaPlugin);
                RUNNING_APPLICATIONS.put(javaPlugin, bukkitApplication);

                bukkitApplication.start();
            }
        } catch (ClassNotFoundException | CircularDependencyException | BeanCreationException | UnsatisfiedDependencyException e) {
            e.printStackTrace();
        }
    }

    public static void shutdownApplication(JavaPlugin javaPlugin) {
        if (RUNNING_APPLICATIONS.containsKey(javaPlugin)) {
            RUNNING_APPLICATIONS.get(javaPlugin).shutdown();
        }
    }

    public static void cleanUp() {
        RUNNING_APPLICATIONS.forEach((j, app) -> {
            app.shutdown();
        });
    }

    @Override
    public void onLoad() {
        this.getLogger().info(Icicle.getLoadText());
    }

    @Override
    public void onEnable() {
        this.getLogger().info("Checking for updates....");
        // TODO update checker

        this.getLogger().info("Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PluginStatusListener(), this);

        this.getLogger().info("Icicle is now READY!");
        this.getLogger().info("Waiting for Icicle-based plugins to be enabled...");
    }

    @Override
    public void onDisable() {
        cleanUp();
    }
}
