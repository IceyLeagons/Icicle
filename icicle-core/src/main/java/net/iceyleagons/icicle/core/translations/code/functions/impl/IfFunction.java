package net.iceyleagons.icicle.core.translations.code.functions.impl;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

import java.util.List;

@CodeFunction
public class IfFunction extends AbstractCodeFunction {

    public IfFunction() {
        super("IF");
    }

    @Override
    public String parse(String input) {
        List<String> functionList = CodeParserUtils.parseFunctionList(CodeParserUtils.getFunctionContent(input));
        if (functionList.size() != 3) return "error";

        String condition = super.getCodeParser().parseFunction(functionList.get(0));
        return condition.equals("true") ? super.getCodeParser().parseFunction(functionList.get(1)) : condition.equals("false") ? super.getCodeParser().parseFunction(functionList.get(2)) : "error";
    }
}
