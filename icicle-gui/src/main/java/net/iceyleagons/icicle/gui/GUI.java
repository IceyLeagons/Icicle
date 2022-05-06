/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.gui;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 16, 2022
 */
@Getter
public abstract class GUI implements Listener {

    private final JavaPlugin javaPlugin;
    private final int rowSize;
    private final int size;
    private final boolean global;

    private final Map<Player, Inventory> viewers = new HashMap<>();
    private final List<Pane> panes = new ArrayList<>();

    private Inventory globalInventory = null;
    private Pane activePane = null;

    public GUI(JavaPlugin javaPlugin, boolean global, int rowSize, int size) {
        this.javaPlugin = javaPlugin;
        this.javaPlugin.getServer().getPluginManager().registerEvents(this, this.javaPlugin);
        this.global = global;
        this.rowSize = rowSize;
        this.size = size;
    }

    public void open(Player player) {
        Inventory inv = getInventory(player);
        viewers.put(player, inv);

        if (panes.size() != 0)
            panes.get(0).render(inv);

        player.openInventory(inv);
    }

    public void addPane(Pane pane) {
        this.panes.add(pane);
        this.activePane = pane;
        pane.setGui(this);
    }

    public Pane createPane() {
        return createPane(this.rowSize, this.size / this.rowSize);
    }

    public Pane createPane(int w, int h) {
        Pane pane = new Pane(w, h, this.rowSize);
        addPane(pane);

        return pane;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        if (!viewers.containsKey(player)) return;

        if (Objects.equals(viewers.get(player), event.getClickedInventory())) {
            activePane.onClick(event, this);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (!viewers.containsKey(player)) return;
        viewers.remove(player);
    }

    public void update() {
        if (viewers.isEmpty()) return;
        if (activePane != null && activePane.isDirty()) {
            if (global && globalInventory != null) {
                activePane.render(globalInventory);
                return;
            }

            for (Inventory value : viewers.values()) {
                activePane.render(value);
            }
        }
    }

    private Inventory getInventory(Player player) {
        if (isGlobal() && this.globalInventory == null) {
            this.globalInventory = getInventory((InventoryHolder) null);
        }

        return isGlobal() ? this.globalInventory : getInventory((InventoryHolder) player);
    }

    protected abstract Inventory getInventory(InventoryHolder holder);

}
