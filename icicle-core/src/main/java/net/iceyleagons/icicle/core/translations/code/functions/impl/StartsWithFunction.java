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

package net.iceyleagons.icicle.core.translations.code.functions.impl;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.List;

@CodeFunction
public class StartsWithFunction extends AbstractCodeFunction {

    public StartsWithFunction() {
        super("SW");
    }

    @Override
    public String parse(String input) {
        List<String> list = CodeParserUtils.parseCommaSeparatedList(CodeParserUtils.getFunctionContent(input));

        if (list.size() <= 1) return "error";
        String value = super.getCodeParser().parseFunction(list.get(0));

        for (int i = 1; i < list.size(); i++) {
            if (value.startsWith(super.getCodeParser().parseFunction(list.get(i))))
                return "true";
        }

        return "false";
    }
}
