/*
 * MIT License
 *
 * Copyright (c) 2020 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
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

package net.iceyleagons.icicle.ui;

import lombok.NonNull;
import net.iceyleagons.icicle.time.SchedulerUtils;
import net.iceyleagons.icicle.ui.components.ComponentTemplate;
import net.iceyleagons.icicle.ui.frame.Frame;
import net.iceyleagons.icicle.ui.guis.BasePaginatedGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Manager and stuff. Yeah epic javadoc.
 *
 * @author TOTHTOMI, Gabe
 * @version 1.0.0
 * @since 1.2.0-SNAPSHOT
 */
public class GUIManager implements Listener {

    private final List<GUITemplate> guis = new ArrayList<>();
    private final JavaPlugin javaPlugin;

    public GUIManager(@NonNull JavaPlugin javaPlugin) {
        this.javaPlugin = javaPlugin;
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    /**
     * Registers the given {@link GUITemplate}
     *
     * @param gui the {@link GUITemplate}
     */
    public void registerGUI(GUITemplate gui) {
        GUI annotation = gui.getClass().getAnnotation(GUI.class);
        if (annotation != null) {
            if (annotation.autoUpdate()) {
                SchedulerUtils.runTaskTimer(javaPlugin, task -> gui.update(),
                        annotation.updateInterval(), annotation.updateIntervalUnit());
            }
            guis.add(gui);
        }
    }

    /**
     * Handles click events inside registered GUIs
     *
     * @param event --
     * @deprecated don't use outside.
     */
    @Deprecated
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Optional<GUITemplate> template = guis.stream().filter(inv -> Objects.equals(event.getClickedInventory(), inv.getInventory())).findFirst();
        if (!template.isPresent()) {
            return;
        }


        GUITemplate temp = template.get();

        Frame frame = temp instanceof BasePaginatedGUI
                ? ((BasePaginatedGUI) temp).getPages().get(((BasePaginatedGUI) temp).getCurrentPage()).get(temp.getCurrentFrame())
                : temp.getFrames().get(temp.getCurrentFrame());

        int slot = event.getSlot();

        event.setCancelled(true);
        ComponentTemplate componentTemplate = frame.onClick(slot).join();

        if (componentTemplate != null) {
            componentTemplate.onClick(new GUIClickEvent() {
                @Override
                public InventoryClickEvent getInventoryClickEvent() {
                    return event;
                }

                @Override
                public GUITemplate getGUI() {
                    return temp;
                }

                @Override
                public ComponentTemplate getSelfComponent() {
                    return componentTemplate;
                }
            });
        }
    }

}
