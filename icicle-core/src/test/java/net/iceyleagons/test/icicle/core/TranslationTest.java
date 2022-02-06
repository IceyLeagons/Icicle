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

package net.iceyleagons.test.icicle.core;

import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 05, 2022
 */
public class TranslationTest {

    private static AbstractCodeFunction[] functions;

    @BeforeAll
    @DisplayName("Collection CodeFunctions")
    public static void setupTranslationService() {
        functions = CodeParser.createFunctionInstances(CodeParser.discoverCodeFunctions(new Reflections("net.iceyleagons.icicle.core.translations"))).toArray(AbstractCodeFunction[]::new);
    }

    @Test
    @DisplayName("Value injection")
    public void testValueInjection() {
        String expected = "Peter says: It works!";
        String result = new CodeParser(functions)
                .addValue("var1", "It works!")
                .addValue("var2", "Peter")
                .parseCode("{var2} says: {var1}");

        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Code test 1")
    public void codeTestCase1() {
        String code = "You have {IF(EQ(amount, 1), 'an', amount)} apple{IF(GT(amount, 1), 's', '')}.";
        String expected1 = "You have an apple.";
        String expected2 = "You have 3 apples.";

        String result1 = new CodeParser(functions)
                .addValue("amount", "1")
                .parseCode(code);

        String result2 = new CodeParser(functions)
                .addValue("amount", "3")
                .parseCode(code);

        Assertions.assertEquals(expected1, result1);
        Assertions.assertEquals(expected2, result2);
    }

    @Test
    @DisplayName("Code test 2")
    public void codeTestCase2() {

    }
}
