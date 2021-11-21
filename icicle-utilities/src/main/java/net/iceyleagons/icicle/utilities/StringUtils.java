package net.iceyleagons.icicle.utilities;

import java.util.Locale;

public class StringUtils {

    public static boolean containsIgnoresCase(String inside, String toContain) {
        return inside.toLowerCase(Locale.ROOT).contains(toContain.toLowerCase(Locale.ROOT));
    }

    public static String toCamelCase(String input) {
        return toCamelCase(input, ' ');
    }

    public static String toCamelCase(String input, char delimiter) {
        StringBuilder sb = new StringBuilder();
        boolean convertLow = true;

        for (char c : input.toCharArray()) {
            if (c == delimiter) convertLow = false;
            else if (convertLow) sb.append(Character.toLowerCase(c));
            else {
                sb.append(Character.toUpperCase(c));
                convertLow = true;
            }
        }

        return sb.toString();
    }
}
