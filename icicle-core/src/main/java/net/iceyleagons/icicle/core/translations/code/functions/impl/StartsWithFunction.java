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
