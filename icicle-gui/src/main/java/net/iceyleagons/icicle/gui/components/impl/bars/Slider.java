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

package net.iceyleagons.icicle.gui.components.impl.bars;

import lombok.EqualsAndHashCode;
import net.iceyleagons.icicle.gui.GuiInteractEvent;
import net.iceyleagons.icicle.gui.Pane;
import net.iceyleagons.icicle.gui.components.Interactable;
import net.iceyleagons.icicle.gui.utils.Pos2i;
import org.bukkit.inventory.ItemStack;

import java.util.function.BiConsumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@EqualsAndHashCode(callSuper = false)
public class Slider extends ProgressBar implements Interactable {

    private final BiConsumer<Integer, GuiInteractEvent> onChanged;

    public Slider(int x, int y, ItemStack filled, ItemStack background, BiConsumer<Integer, GuiInteractEvent> onChanged) {
        super(x, y, filled, background);
        this.onChanged = onChanged;
    }

    public Slider(int x, int y, int w, int h, ItemStack filled, ItemStack background, BiConsumer<Integer, GuiInteractEvent> onChanged) {
        super(x, y, w, h, filled, background);
        this.onChanged = onChanged;
    }

    @Override
    public void onClick(GuiInteractEvent guiInteractEvent) {
        final Pos2i pos = guiInteractEvent.getClickPosition();
        int clickedX = pos.getX();
        // TODO we ignore Y because X is our main coordinate. If rotations are implemented then it will become necessary.

        final Pane pane = guiInteractEvent.getPane();
        int startX = this.getX() + pane.getXOff();

        this.setValue(clickedX - startX + 1);
        if (this.onChanged != null) {
            this.onChanged.accept(this.getValue(), guiInteractEvent);
        }
        this.requestGuiUpdate();
    }
}
