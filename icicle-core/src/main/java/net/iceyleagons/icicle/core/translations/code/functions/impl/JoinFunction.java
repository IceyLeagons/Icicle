package net.iceyleagons.icicle.core.translations.code.functions.impl;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.ArrayList;
import java.util.List;

@CodeFunction
public class JoinFunction extends AbstractCodeFunction {

    public JoinFunction() {
        super("JOIN");
    }

    @Override
    public String parse(String input) {
        List<String> list = CodeParserUtils.parseFunctionList(CodeParserUtils.getFunctionContent(input));
        if (list.size() < 2) return "error";

        String delimiter = super.getCodeParser().parseFunction(list.get(0));
        List<String> parsed = new ArrayList<>();

        for (int i = 1; i < list.size(); i++) {
            parsed.add(super.getCodeParser().parseFunction(list.get(i)));
        }

        return String.join(delimiter, parsed);
    }
}
