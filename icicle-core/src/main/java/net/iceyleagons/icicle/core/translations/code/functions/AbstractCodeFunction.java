package net.iceyleagons.icicle.core.translations.code.functions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.CodeParserUtils;

import java.util.List;

@Getter
@RequiredArgsConstructor
public abstract class AbstractCodeFunction {

    private final String functionName;

    @Setter
    private CodeParser codeParser;

    public abstract String parse(String input);

    protected String handleSimpleList(String input, SizeFilter sizeFilter, ReturnValueSupplier returnValueSupplier) {
        List<String> list = CodeParserUtils.parseCommaSeparatedList(CodeParserUtils.getFunctionContent(input));

        if (sizeFilter.isAllowed(list.size())) return "error";

        String val1 = getValueIfExists(list.get(0));
        String val2 = getValueIfExists(list.get(1));

        return returnValueSupplier.get(val1, val2);
    }

    protected boolean isInteger(String str) {
        System.out.println("Checking int for: " + str);
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected boolean isBoolean(String str) {
        return str.equals("true") || str.equals("false");
    }

    protected int parseInt(String str) {
        return Integer.parseInt(str);
    }

    protected String getValueIfExists(String key) {
        if (CodeParserUtils.isStringPart(key)) return CodeParserUtils.getStringContent(key);

        return codeParser.getValues().getOrDefault(key, key);
    }
}
