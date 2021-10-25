package net.iceyleagons.icicle.core.translations.code.functions.impl.constants;

import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class FalseConstant extends AbstractCodeFunction {
    public FalseConstant() {
        super("FALSE");
    }

    @Override
    public String parse(String input) {
        return "false";
    }
}

