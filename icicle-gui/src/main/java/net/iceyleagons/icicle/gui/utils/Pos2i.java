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

package net.iceyleagons.icicle.gui.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pos2i {

    private int x;
    private int y;

    public static Pos2i from(int slot, int rowSize) {
        return InventoryUtils.calculateXYFromSlot(slot, rowSize);
    }

    public int toInventorySlot(int rowSize) {
        return InventoryUtils.calculateSlotFromXY(x, y, rowSize);
    }

    public Pos2i rotateClockwise(int rot, int w, int h) {
        return InventoryUtils.rotateClockwise(x, y, w, h, rot);
    }

    public Pos2i rotateCounterClockwise(int rot, int w, int h) {
        return InventoryUtils.rotateCounterClockwise(x, y, w, h, rot);
    }

    @Override
    public String toString() {
        return "Pos2i[x=" + x + ", y=" + y + "]";
    }
}
