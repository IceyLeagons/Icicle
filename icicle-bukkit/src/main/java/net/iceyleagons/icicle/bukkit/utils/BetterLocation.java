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

import net.iceyleagons.icicle.utilities.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of Location to be faster in hash maps etc.
 * The implementation of equals sacrifises
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 22, 2022
 */
public class BetterLocation extends Location {

    public BetterLocation(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public BetterLocation(World world, double x, double y, double z, float yaw, float pitch) {
        super(world, x, y, z, yaw, pitch);
    }

    public String serializeToString(String delimiter) {
        return String.join(delimiter, Objects.requireNonNull(super.getWorld()).getName(),
                String.valueOf(super.getX()), String.valueOf(super.getY()), String.valueOf(super.getZ()), String.valueOf(super.getYaw()), String.valueOf(super.getPitch()));
    }

    public static BetterLocation fromString(String input, String delimiter) {
        String[] data = input.split(delimiter);
        return new BetterLocation(Bukkit.getWorld(data[0]), Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), Float.parseFloat(data[4]), Float.parseFloat(data[5]));
    }

    public Location asLocation() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj instanceof BetterLocation) {
            BetterLocation loc = (BetterLocation) obj;
            return Objects.equals(super.getWorld(), loc.getWorld()) &&
                    Objects.equals(super.getChunk(), loc.getChunk()) &&
                    super.getX() == loc.getX() && super.getY() == loc.getY() && super.getZ() == loc.getZ() &&
                    super.getYaw() == loc.getYaw() && super.getPitch() == loc.getPitch();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int) MathUtils.better3DHash((int) super.getX(), (int) super.getY(), (int) super.getZ());
    }
}
