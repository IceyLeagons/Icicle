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

import net.iceyleagons.icicle.utilities.lang.Utility;

import java.util.Locale;

@Utility
public final class StringUtils {

    public static int getLongestLength(String... lines) {
        int longest = 0;
        for (String line : lines) {
            int length = line.length();
            if (length > longest) {
                longest = length;
            }
        }

        return longest;
    }

    public static String putInCenter(String text, String toPutInCenter) {
        StringBuilder stringBuilder = new StringBuilder(text);
        stringBuilder.insert(text.length() / 2, toPutInCenter);
        return stringBuilder.toString();
    }

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

    public static String nullToEmpty(String input) {
        return input == null ? "" : input;
    }

    public static String emptyToNull(String input) {
        return input.isEmpty() ? null : input;
    }
}
