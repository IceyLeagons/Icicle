/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

import java.util.Locale;
import java.util.Objects;

public class StringUtils {

    public static boolean containsIgnoresCase(String inside, String toContain) {
        return inside.toLowerCase(Locale.ROOT).contains(toContain.toLowerCase(Locale.ROOT));
    }

    public static String toCamelCase(String input) {
        return toCamelCase(input, ' ');
    }

    public static String toCamelCase(String input, char delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean convertLow = false;

        for (char c : input.toCharArray()) {
            if (c == delimiter) convertLow = false;
            else if (convertLow) sb.append(Character.toLowerCase(c));
            else {
                sb.append(delimiter).append(Character.toUpperCase(c));
                convertLow = true;
            }
        }

        return sb.substring(1);
    }

    public static int calculateLevenshteinDistance(String a, String b) {
        int aLimit = a.length() + 1;
        int bLimit = b.length() + 1;
        int[][] distance = new int[aLimit][];
        for (int i = 0; i < aLimit; ++i) {
            distance[i] = new int[bLimit];
        }
        for (int i = 0; i < aLimit; ++i) {
            distance[i][0] = i;
        }
        for (int j = 0; j < bLimit; ++j) {
            distance[0][j] = j;
        }
        for (int i = 1; i < aLimit; ++i) {
            for (int j = 1; j <  bLimit; ++j) {
                char aChar = a.charAt(i - 1);
                char bChar = b.charAt(j - 1);
                distance[i][j] = findMin(
                        distance[i - 1][j] + 1,
                        distance[i][j - 1] + 1,
                        distance[i - 1][j - 1] + (Objects.equals(aChar, bChar) ? 0 : 1) // + substitution cost
                );
            }
        }
        return distance[a.length()][b.length()];
    }

    private static int findMin(int a, int b, int c) {
        int min = Math.min(a, b);
        return Math.min(min, c);
    }
}
