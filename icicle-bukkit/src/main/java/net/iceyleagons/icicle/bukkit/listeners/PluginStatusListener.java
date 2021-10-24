package net.iceyleagons.icicle.bukkit.listeners;

import net.iceyleagons.icicle.bukkit.IcicleBukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginStatusListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginEnabled(PluginEnableEvent event) {
        if (event.getPlugin() instanceof JavaPlugin) {
            IcicleBukkit.startNewApplication((JavaPlugin) event.getPlugin());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPluginDisabled(PluginDisableEvent event) {
        if (event.getPlugin() instanceof JavaPlugin) {
            IcicleBukkit.shutdownApplication((JavaPlugin) event.getPlugin());
        }
    }
}
