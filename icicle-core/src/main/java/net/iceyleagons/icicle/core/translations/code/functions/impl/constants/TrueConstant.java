package net.iceyleagons.icicle.core.translations.code.functions.impl.constants;

import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class TrueConstant extends AbstractCodeFunction {
    public TrueConstant() {
        super("TRUE");
    }

    @Override
    public String parse(String input) {
        return "true";
    }
}
