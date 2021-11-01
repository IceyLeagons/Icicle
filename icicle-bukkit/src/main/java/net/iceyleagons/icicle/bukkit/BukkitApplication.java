package net.iceyleagons.icicle.bukkit;

import net.iceyleagons.icicle.bukkit.impl.BukkitExecutionHandler;
import net.iceyleagons.icicle.core.AbstractIcicleApplication;
import net.iceyleagons.icicle.core.utils.ExecutionHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitApplication extends AbstractIcicleApplication {

    private final JavaPlugin javaPlugin;
    private final BukkitExecutionHandler executionHandler;
    public BukkitApplication(String rootPackage, JavaPlugin javaPlugin) {
        super(rootPackage);
        this.javaPlugin = javaPlugin;
        this.executionHandler = new BukkitExecutionHandler(javaPlugin);

        super.getBeanManager().getBeanRegistry().registerBean(BukkitApplication.class, this);
        super.getBeanManager().getBeanRegistry().registerBean(Plugin.class, javaPlugin);
        super.getBeanManager().getBeanRegistry().registerBean(JavaPlugin.class, javaPlugin);
        super.getBeanManager().getBeanRegistry().registerBean(PluginManager.class, Bukkit.getPluginManager());
    }

    @Override
    public ExecutionHandler getExecutionHandler() {
        return this.executionHandler;
    }
}
