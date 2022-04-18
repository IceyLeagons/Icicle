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

package net.iceyleagons.icicle.gui.components.impl.buttons;

import net.iceyleagons.icicle.gui.GuiInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 18, 2022
 */
public class ToggleButton extends LabelButton {

    private boolean value = false;
    private final ItemStack onItem;
    private final ItemStack offItem;
    private final Consumer<GuiInteractEvent> handler;

    public ToggleButton(int x, int y, ItemStack onItem, ItemStack offItem, BiConsumer<Boolean, GuiInteractEvent> onClicked) {
        super(x, y, offItem, null);
        this.onItem = onItem;
        this.offItem = offItem;
        this.handler = getHandler(onClicked);
    }

    public ToggleButton(int x, int y, int w, int h, ItemStack onItem, ItemStack offItem, BiConsumer<Boolean, GuiInteractEvent> onClicked) {
        super(x, y, w, h, offItem, null);
        this.onItem = onItem;
        this.offItem = offItem;
        this.handler = getHandler(onClicked);
    }

    private Consumer<GuiInteractEvent> getHandler(final BiConsumer<Boolean, GuiInteractEvent> onClicked) {
        return guiInteractEvent -> {
            setValue(!getValue());
            onClicked.accept(getValue(), guiInteractEvent);
        };
    }

    public boolean getValue() {
        return this.value;
    }

    public ToggleButton setValue(boolean value) {
        this.value = value;
        this.setItemStack(value ? this.onItem : this.offItem);
        this.setDirty(true);
        this.requestGuiUpdate();
        return this;
    }

    @Override
    protected Consumer<GuiInteractEvent> getClickHandler() {
        return this.handler;
    }
}
