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
import net.iceyleagons.icicle.utilities.MathUtils;
import org.bukkit.inventory.ItemStack;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 18, 2022
 */
@EqualsAndHashCode(callSuper = false)
public class PercentageBar extends ProgressBar {

    public PercentageBar(int x, int y, ItemStack filled, ItemStack background) {
        super(x, y, filled, background);
    }

    public PercentageBar(int x, int y, int w, int h, ItemStack filled, ItemStack background) {
        super(x, y, w, h, filled, background);
    }

    @Override
    public PercentageBar setValue(int value) {
        super.setValue(Math.min(MathUtils.map(value, 0, 100, 1, super.getWidth()), super.getWidth()));
        return this;
    }
}