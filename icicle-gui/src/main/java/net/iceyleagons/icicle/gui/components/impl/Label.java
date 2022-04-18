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

package net.iceyleagons.icicle.gui.components.impl;

import lombok.EqualsAndHashCode;
import net.iceyleagons.icicle.gui.DrawUtils;
import net.iceyleagons.icicle.gui.Pane;
import net.iceyleagons.icicle.gui.components.GuiComponent;
import org.bukkit.inventory.ItemStack;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@EqualsAndHashCode(callSuper = false)
public class Label extends GuiComponent {

    private final ItemStack itemStack;

    public Label(int x, int y, ItemStack itemStack) {
        super(x, y);
        this.itemStack = itemStack;
    }

    public Label(int x, int y, int w, int h, ItemStack itemStack) {
        super(x, y, w, h);
        this.itemStack = itemStack;
    }

    @Override
    public void draw(Pane pane, int xOff, int yOff, int mW, int mH) {
        DrawUtils.basicLabelDraw(pane, getX(), getY(), xOff, yOff, getWidth(), getHeight(), mW, mH, this.itemStack);
    }
}
