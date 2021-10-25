package net.iceyleagons.icicle.core.translations.code.functions.impl.number.checks;

import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;

@CodeFunction
public class LessThanOrEqualToFunction extends AbstractCodeFunction {

    public LessThanOrEqualToFunction() {
        super("LTEQ");
    }

    @Override
    public String parse(String input) {
        return super.handleSimpleList(input, s -> s != 2, (v1, v2) -> {
            if (!super.isInteger(v1) || !super.isInteger(v2)) return "error";
            return String.valueOf(super.parseInt(v1) <= super.parseInt(v2));
        });
    }
}
