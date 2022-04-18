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

import net.iceyleagons.icicle.utilities.lang.Utility;
import org.bukkit.inventory.ItemStack;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@Utility
public final class DrawUtils {

    public static void drawFillBar(Pane pane, int x, int y, int xOff, int yOff, int width, int height, int mW, int mH, int filled, ItemStack bi, ItemStack fi) {
        int w = Math.min(width, mW);
        int h = Math.min(height, mH);

        int dX = getDelta(x, xOff); // coordinates start from 1, we need 0 + our offest
        int dY = getDelta(y, yOff);


        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pane.setItem(i + dX, j + dY, i < filled ? fi : bi);
            }
        }
    }

    public static void basicLabelDraw(Pane pane, int x, int y, int xOff, int yOff, int width, int height, int mW, int mH, ItemStack itemStack) {
        int w = Math.min(width, mW);
        int h = Math.min(height, mH);

        int dX = getDelta(x, xOff); // coordinates start from 1, we need 0 + our offest
        int dY = getDelta(y, yOff);


        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                pane.setItem(i + dX, j + dY, itemStack);
            }
        }
    }

    private static int getDelta(int val, int off) {
        return (val - 1) + off;
    }

}
