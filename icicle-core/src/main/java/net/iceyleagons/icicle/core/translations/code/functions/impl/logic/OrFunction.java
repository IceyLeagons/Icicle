package net.iceyleagons.icicle.core.translations.code.functions.impl.logic;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.List;

@CodeFunction
public class OrFunction extends AbstractCodeFunction {

    public OrFunction() {
        super("OR");
    }

    @Override
    public String parse(String input) {
        List<String> list = CodeParserUtils.parseFunctionList(CodeParserUtils.getFunctionContent(input));
        if (list.size() == 0) return "error";

        for (String s : list) {
            if (isBoolean(s)) return "error";

            String parsed = super.getCodeParser().parseFunction(s);
            if (parsed.equals("true")) return "true";
            else if (!parsed.equals("false")) return "error";
        }

        return "false";
    }
}
