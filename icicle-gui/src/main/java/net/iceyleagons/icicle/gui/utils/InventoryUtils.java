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

import org.bukkit.inventory.ItemStack;

import static net.iceyleagons.icicle.utilities.ErrorUtils.ILLEGAL;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 17, 2022
 */
public class InventoryUtils {

    /**
     * This is used to bring a 2D coordinate system for the inventory.
     * X and Y must start with 1. (1 is subtracted from them)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the calculated slot
     */
    public static int calculateSlotFromXY(int x, int y, int rowSize) {
        if (x <= 0 || y <= 0) ILLEGAL("X and Y must start at 1");

        return (x - 1) + (y - 1) * rowSize;
    }

    /**
     * This will reverse our {@link #calculateSlotFromXY(int, int)}.
     *
     * @param slot the inventory slot
     * @return an int array described above
     */
    public static Pos2i calculateXYFromSlot(int slot, int rowSize) {
        return new Pos2i(slot % rowSize + 1, slot / rowSize + 1);
    }

    public static Pos2i rotateCounterClockwise(int x, int y, int w, int h, int rotation) {
        return rotateClockwise(x, y, w, h, 360 - rotation);
    }

    public static Pos2i rotateClockwise(int x, int y, int w, int h, int rotation) {
        return switch (rotation) {
            case 90 -> new Pos2i(h - 1 - y, x);
            case 180 -> new Pos2i(w - 1 - x, h - 1 - y);
            case 270 -> new Pos2i(y, w - 1 - x);
            default -> new Pos2i(x, y);
        };
    }
}
