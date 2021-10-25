package net.iceyleagons.icicle.core.translations.code.functions.impl;

import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class EqualsFunction extends AbstractCodeFunction {

    public EqualsFunction() {
        super("EQ");
    }

    @Override
    public String parse(String input) {
        return super.handleSimpleList(input, s -> s != 2, (v1, v2) -> String.valueOf(v1.equals(v2)));
    }
}
