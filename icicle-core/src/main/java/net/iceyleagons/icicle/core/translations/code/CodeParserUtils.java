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

package net.iceyleagons.icicle.core.translations.code;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.iceyleagons.icicle.core.translations.code.CodeParser.*;

public class CodeParserUtils {

    public static String getFunctionBody(String input, int start) {
        int currentIndex = start;

        int a = 0; // apostrophe (string part) index
        int cs = 0; //code part start index
        int ce = 0; //code part end index

        int fs = 0; //function part start index
        int fe = 0; //function part end index

        while (input.length() > currentIndex) {
            char symbol = input.charAt(currentIndex);

            // logic of escaping
            if (symbol == '\\') {
                if (currentIndex + 1 == input.length()) return "error";

                char c = input.charAt(currentIndex + 1);
                if (!isSpecialChar(c)) currentIndex--;

                currentIndex += 2;
                continue;
            }

            switch (symbol) {
                case STRING_PART_INDICATOR:
                    a += 1;
                    break;
                case CODE_PART_START:
                    cs += 1;
                    break;
                case CODE_PART_END:
                    ce += 1;
                    break;
                case FUNC_PART_START:
                    fe += 1;
                    break;
                case FUNC_PART_END:
                    fs += 1;
                    break;
                case ',':
                    // Don't even try to understand what's going on here....
                    if (a >= 0 && cs >= 0 && fs >= 0 && (a % 2 == 0) && cs == ce && fs == fe)
                        return input.substring(start, currentIndex);
                    break;
                default:
                    currentIndex++;
                    continue;
            }

            // Don't even try to understand what's going on here....
            if (a >= 0 && cs >= 0 && fs >= 0 && (a % 2 == 0) && cs == ce && fs == fe)
                return input.substring(start, currentIndex + 1);

            currentIndex += 1;
        }

        // Don't even try to understand what's going on here....
        if (a >= 0 && cs >= 0 && fs >= 0 && (a % 2 == 0) && cs == ce && fs == fe)
            return input.substring(start);

        return "error";
    }

    public static boolean isStringPart(String input) {
        int a = input.indexOf(STRING_PART_INDICATOR);
        int b = input.lastIndexOf(STRING_PART_INDICATOR);

        return a != -1 && b != -1 && a < b;
    }

    public static String getStringContent(String input) {
        int a = input.indexOf(STRING_PART_INDICATOR);
        int b = input.lastIndexOf(STRING_PART_INDICATOR);

        return a != -1 && b != -1 && a < b ? input.substring(a + 1, b) : input;
    }

    public static String getContent(String input, char start, char end) {
        int a = input.indexOf(start);
        int b = input.lastIndexOf(end);

        return a == -1 || b == -1 ? input : input.substring(a + 1, b);
    }

    public static String getFunctionName(String input, char start) {
        int a = input.indexOf(start);
        return a == -1 ? "" : input.substring(0, a).trim().toLowerCase();
    }

    public static boolean hasParsableCode(String input) {
        return hasParsableCode(input, 0);
    }

    public static boolean hasParsableCode(String input, int start) {
        int a = input.indexOf(CODE_PART_START, start);
        int b = input.lastIndexOf(CODE_PART_END);

        return start >= 0 && a != -1 && b != -1 && a < b;
    }

    public static boolean isSpecialChar(char symbol) {
        return symbol == '\'' || symbol == FUNC_PART_START || symbol == FUNC_PART_END || symbol == CODE_PART_START || symbol == CODE_PART_END;
    }

    public static List<String> parseCommaSeparatedList(String input) {
        return Arrays.stream(input.split(",")).map(String::trim).collect(Collectors.toList());
    }

    public static String getFunctionContent(String input) {
        return getContent(input, FUNC_PART_START, FUNC_PART_END);
    }

    public static List<String> parseFunctionList(String input) {
        int start = 0;
        List<String> list = new ArrayList<>();

        while (input.indexOf(",", start) != -1) {
            String body = getFunctionBody(input, start);
            if (body.equals("error")) return list;

            int index = input.indexOf(body, start);
            int comma = input.indexOf(",", index + body.length());
            if (comma == -1) break;

            start = comma + 1;
            list.add(body.trim());
        }

        String last = getFunctionBody(input, start);
        if (last.equals("error")) return list;

        list.add(last.trim());

        return list;
    }
}
