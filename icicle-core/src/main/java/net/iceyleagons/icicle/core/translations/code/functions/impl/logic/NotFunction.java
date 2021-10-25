package net.iceyleagons.icicle.core.translations.code.functions.impl.logic;

import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class NotFunction extends AbstractCodeFunction {

    public NotFunction() {
        super("NOT");
    }


    @Override
    public String parse(String input) {
        String content = CodeParserUtils.getFunctionContent(input);
        if (isBoolean(content)) return "error";

        String parsed = super.getCodeParser().parseFunction(content);
        return isBoolean(parsed) ? String.valueOf(!Boolean.parseBoolean(parsed)) : "error";
    }
}
