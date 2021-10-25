package net.iceyleagons.icicle.core.translations.code.functions.impl.logic;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.List;

@CodeFunction
public class AndFunction extends AbstractCodeFunction {

    public AndFunction() {
        super("AND");
    }

    @Override
    public String parse(String input) {
        List<String> list = CodeParserUtils.parseFunctionList(CodeParserUtils.getFunctionContent(input));

        for (String s : list) {
            if (isBoolean(s)) return "error";
            String parsed = super.getCodeParser().parseFunction(s);

            if (parsed.equals("false")) return "false";
            else if (!parsed.equals("true")) return "error";
        }

        return "true";
    }
}
