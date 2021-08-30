package net.iceyleagons.icicle.utilities;

import java.util.Locale;

public class StringUtils {

    public static boolean containsIgnoresCase(String inside, String toContain) {
        return inside.toLowerCase(Locale.ROOT).contains(toContain.toLowerCase(Locale.ROOT));
    }

}
