package net.iceyleagons.icicle.core.translations.code.functions.impl.number;

import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class MultiplyFunction extends AbstractCodeFunction {

    public MultiplyFunction() {
        super("MUL");
    }

    @Override
    public String parse(String input) {
        return super.handleSimpleList(input, s -> s != 2, (v1, v2) -> {
            if (!isInteger(v1) || !isInteger(v2)) return "error";
            return String.valueOf(parseInt(v1) * parseInt(v2));
        });
    }
}
