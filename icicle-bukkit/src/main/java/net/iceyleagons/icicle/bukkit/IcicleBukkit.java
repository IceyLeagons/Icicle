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

package net.iceyleagons.icicle.bukkit;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.bukkit.listeners.PluginStatusListener;
import net.iceyleagons.icicle.core.GlobalBeanRegistry;
import net.iceyleagons.icicle.core.Icicle;
import net.iceyleagons.icicle.core.annotations.AutoCreate;
import net.iceyleagons.icicle.core.annotations.IcicleApplication;
import net.iceyleagons.icicle.core.maven.MavenLibraryLoader;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.Map;

@IcicleApplication("net.iceyleagons.icicle.bukkit")
public class IcicleBukkit extends JavaPlugin {

    public static final Map<JavaPlugin, BukkitApplication> RUNNING_APPLICATIONS = new HashMap<>();

    public static void startNewApplication(JavaPlugin javaPlugin) {
        try {
            System.out.println("Starting");
            Class<?> clazz = Class.forName(javaPlugin.getDescription().getMain());

            if (clazz.isAnnotationPresent(IcicleApplication.class)) {
                String mainPackage = clazz.getAnnotation(IcicleApplication.class).value();

                BukkitApplication bukkitApplication = new BukkitApplication(mainPackage, javaPlugin);
                RUNNING_APPLICATIONS.put(javaPlugin, bukkitApplication);

                bukkitApplication.start();
            }
        } catch (Exception e) {
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
        GlobalBeanRegistry.INSTANCE.cleanUp();
    }

    @Override
    public void onLoad() {
        this.getLogger().info(Icicle.getLoadText());


        //GlobalBeanRegistry.registerService(Server.class, Bukkit.getServer());
        //GlobalBeanRegistry.registerService(PluginManager.class, Bukkit.getPluginManager());
        //GlobalBeanRegistry.registerService(BukkitScheduler.class, Bukkit.getScheduler());
    }

    @SneakyThrows
    @Override
    public void onEnable() {
        this.getLogger().info("Checking for updates....");
        // TODO update checker
        System.out.println("Loading");
        MavenLibraryLoader.load("net.iceyleagons", "icicle-commands", "1.0-SNAPSHOT", "https://mvn.iceyleagons.net/snapshots/");
        try {
            System.out.println(Class.forName("net.iceyleagons.icicle.commands.CommandService"));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Registering listeners...");
        Bukkit.getPluginManager().registerEvents(new PluginStatusListener(), this);

        this.getLogger().info("Icicle is now READY!");
        this.getLogger().info("Waiting for Icicle-based plugins to be enabled...");

        //startNewApplication(this);
    }

    @Override
    public void onDisable() {
        cleanUp();
    }
}
