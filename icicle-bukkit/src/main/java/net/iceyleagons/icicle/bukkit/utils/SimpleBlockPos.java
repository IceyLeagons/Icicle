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

package net.iceyleagons.icicle.bukkit.utils;

import lombok.AllArgsConstructor;
import net.iceyleagons.icicle.utilities.MathUtils;
import org.bukkit.block.Block;

import java.util.HashSet;
import java.util.Set;

/**
 * Very simple block position data class.
 * (Prepared for AI framework down the line)
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 22, 2022
 */
@AllArgsConstructor
public class SimpleBlockPos {

    private int x;
    private int y;
    private int z;

    public SimpleBlockPos(Block block) {
        this.x = block.getX();
        this.y = block.getY();
        this.z = block.getZ();
    }

    public SimpleBlockPos up(int amount) {
        return new SimpleBlockPos(x, y + amount, z);
    }

    public SimpleBlockPos down(int amount) {
        return new SimpleBlockPos(x, y - amount, z);
    }

    public Set<SimpleBlockPos> getNeighbors() {
        Set<SimpleBlockPos> set = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0 && z == 0) continue;
                    set.add(new SimpleBlockPos(this.x + x, this.y + x, this.z + z));
                }
            }
        }

        return set;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SimpleBlockPos)) {
            return false;
        }

        return obj.hashCode() == this.hashCode();
    }

    @Override
    public int hashCode() {
        return (int) MathUtils.better3DHash(x, y, z);
    }
}
