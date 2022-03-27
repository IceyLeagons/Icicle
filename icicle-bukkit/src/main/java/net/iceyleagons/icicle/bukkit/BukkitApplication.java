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

import lombok.Getter;
import net.iceyleagons.icicle.bukkit.impl.BukkitExecutionHandler;
import net.iceyleagons.icicle.bukkit.impl.BukkitServiceProvider;
import net.iceyleagons.icicle.core.AbstractIcicleApplication;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class BukkitApplication extends AbstractIcicleApplication {

    private final JavaPlugin javaPlugin;

    public BukkitApplication(String rootPackage, JavaPlugin javaPlugin) {
        super(rootPackage, new BukkitExecutionHandler(javaPlugin), new BukkitServiceProvider());
        this.javaPlugin = javaPlugin;

        super.getBeanManager().getBeanRegistry().registerBean(Server.class, javaPlugin.getServer()); //removed due to the introduction of GlobalBeanRegistry
        super.getBeanManager().getBeanRegistry().registerBean(BukkitApplication.class, this);
        super.getBeanManager().getBeanRegistry().registerBean(Plugin.class, javaPlugin);
        super.getBeanManager().getBeanRegistry().registerBean(JavaPlugin.class, javaPlugin);
        super.getBeanManager().getBeanRegistry().registerBean(PluginManager.class, Bukkit.getPluginManager()); //removed due to the introduction of GlobalBeanRegistry
    }

    @Override
    public String getName() {
        return this.javaPlugin.getName();
    }
}
