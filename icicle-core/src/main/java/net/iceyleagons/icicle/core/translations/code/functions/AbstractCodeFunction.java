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

package net.iceyleagons.icicle.core.translations.code.functions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class AbstractCodeFunction {

    private final String functionName;

    @Setter
    private CodeParser codeParser;

    public abstract String parse(String input);

    protected String handleSimpleList(String input, SizeFilter sizeFilter, ReturnValueSupplier returnValueSupplier) {
        List<String> list = CodeParserUtils.parseCommaSeparatedList(CodeParserUtils.getFunctionContent(input));

        if (sizeFilter.isAllowed(list.size())) return "error";

        String val1 = getValueIfExists(list.get(0));
        String val2 = getValueIfExists(list.get(1));

        return returnValueSupplier.get(val1, val2);
    }

    protected boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected boolean isBoolean(String str) {
        return str.equals("true") || str.equals("false");
    }

    protected int parseInt(String str) {
        return Integer.parseInt(str);
    }

    protected String getValueIfExists(String key) {
        if (CodeParserUtils.isStringPart(key)) return CodeParserUtils.getStringContent(key);

        return codeParser.getValues().getOrDefault(key, key);
    }
}
