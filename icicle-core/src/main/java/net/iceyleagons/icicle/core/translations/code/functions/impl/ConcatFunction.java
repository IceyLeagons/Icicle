package net.iceyleagons.icicle.core.translations.code.functions.impl;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.List;

@CodeFunction
public class ConcatFunction extends AbstractCodeFunction {

    public ConcatFunction() {
        super("CONCAT");
    }

    @Override
    public String parse(String input) {
        List<String> list = CodeParserUtils.parseFunctionList(CodeParserUtils.getFunctionContent(input));
        StringBuilder sb = new StringBuilder();

        for (String s : list) {
            String parsed = super.getCodeParser().parseFunction(s);
            if (parsed.equals("error")) return "error";

            sb.append(parsed);
        }

        return sb.toString();
    }
}
