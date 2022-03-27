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

package net.iceyleagons.icicle.utilities;

import net.iceyleagons.icicle.utilities.lang.Utility;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 22, 2022
 */
@Utility
public final class MathUtils {

    public static long better3DHash(int x, int y, int z) {
        // Taken from the Baritone project. (https://github.com/cabaletta/baritone/blob/master/src/api/java/baritone/api/utils/BetterBlockPos.java)
        long hash = 3241;
        hash = 3457689L * hash + x;
        hash = 8734625L * hash + y;
        hash = 2873465L * hash + z;
        return hash;
    }

    /**
     * According to my tests you will usually do this in the same amount of time
     * than "regular" way of doing this.
     * <p>
     * However at scenarios, where it will be many times in a row it will be useful.
     * <p>
     * Based completely off of: https://en.wikipedia.org/wiki/Fast_inverse_square_root
     *
     * @param number the number to find the inv. sqrt. for
     * @return the inv. sqrt. !APPROXIMATION! of the number
     */
    public static float fastInvSqrt(float number) {
        float localNumber = number;
        float xh = 0.5f * localNumber;

        int i = Float.floatToIntBits(localNumber);
        i = 0x5f3759df - (i >> 1);
        localNumber = Float.intBitsToFloat(i);

        localNumber *= (1.5f - xh * localNumber * localNumber);
        localNumber *= (1.5f - xh * localNumber * localNumber);
        //localNumber *= (1.5f - xh * localNumber * localNumber);
        return localNumber;
    }
}
