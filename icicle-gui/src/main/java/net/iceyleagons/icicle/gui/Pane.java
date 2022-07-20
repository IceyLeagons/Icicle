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

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.gui.components.GuiComponent;
import net.iceyleagons.icicle.gui.components.Interactable;
import net.iceyleagons.icicle.gui.utils.InventoryUtils;
import net.iceyleagons.icicle.gui.utils.Pos2i;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

import static net.iceyleagons.icicle.utilities.ErrorUtils.ILLEGAL;

/**
 * A pane represents the "renderer" for the GUI. It holds the components and have them render to a matrix of ItemStacks.
 * It's also the first object down the chain, which is {@link StateAware}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@EqualsAndHashCode
public class Pane implements StateAware {

    @Getter
    private final Set<GuiComponent> components = new ObjectOpenHashSet<>();

    @Setter
    @Getter
    private GUI gui;

    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final int rowSize;

    private final ItemStack[][] items;

    @Getter
    private final int xOff = 0, yOff = 0; // easy access

    private boolean dirty = true;

    public Pane(int width, int height, int rowSize) {
        this.width = width;
        this.height = height;
        this.rowSize = rowSize;
        this.items = new ItemStack[width][height];
    }

    public void render(Inventory inventory) {
        updateItems();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                inventory.setItem(InventoryUtils.calculateSlotFromXY(i + 1, j + 1, this.rowSize), items[i][j]);
            }
        }
    }

    private void updateItems() {
        if (!isDirty()) return;

        for (GuiComponent component : components) {
            if (!component.isDirty() || !component.isVisible()) continue;
            component.draw(this, xOff, yOff, this.width, this.height);
            component.setDirty(false);
        }
        setDirty(false);
    }

    public Pane addComponent(GuiComponent component) {
        this.components.add(component);
        component.setParent(this);
        setDirty(true);
        return this;
    }

    public void onClick(InventoryClickEvent event, GUI gui) {
        event.setCancelled(true); // By default we want to cancel, so non-interactable items cannot be taken out
        for (GuiComponent component : components) {
            if (!(component instanceof Interactable)) continue;

            if (component.isPartOf(this.rowSize, event.getSlot(), xOff, yOff, this.width, this.height)) {
                Interactable interactable = (Interactable) component;
                interactable.onClick(gui, this, event, Pos2i.from(event.getSlot(), this.rowSize));
            }
        }
    }

    public boolean hasItems() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (hasItemAtPos(new Pos2i(i, j))) return true;
            }
        }

        return false;
    }

    public Pane setItem(int x, int y, ItemStack itemStack) {
        return setItem(new Pos2i(x, y), itemStack);
    }

    public Pane fill(ItemStack itemStack) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setItem(i, j, itemStack);
            }
        }

        return this;
    }

    public Pane setItem(Pos2i pos2i, ItemStack itemStack) {
        assertBounds(pos2i);
        this.items[pos2i.getX()][pos2i.getY()] = itemStack;
        setDirty(true);
        return this;
    }

    public boolean hasItemAtPos(Pos2i pos2i) {
        return getItem(pos2i) != null;
    }

    public ItemStack getItem(Pos2i pos2i) {
        assertBounds(pos2i);
        return this.items[pos2i.getX()][pos2i.getY()];
    }

    private boolean isInBounds(Pos2i pos) {
        return isInBounds(pos.getX(), pos.getY());
    }

    private boolean isInBounds(int x, int y) {
        return isInBounds(0, width - 1, x) && isInBounds(0, height - 1, y);
    }

    private boolean isInBounds(int min, int max, int value) {
        return min <= value && value <= max;
    }

    private void assertBounds(Pos2i pos2i) {
        if (!isInBounds(pos2i)) {
            ILLEGAL(String.format("Position must be in-bounds! Given values: x = %s | y = %s. Allowed values: x = %s | y = %s", pos2i.getX(), pos2i.getY(), width, height));
        }
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    @Override
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
