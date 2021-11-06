package net.iceyleagons.icicle.core.translations.code;

import lombok.Getter;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.code.functions.CodeFunction;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static net.iceyleagons.icicle.core.translations.code.CodeParserUtils.*;

@Getter
public class CodeParser {

    public static final char CODE_PART_START = '{';
    public static final char CODE_PART_END = '}';
    public static final char FUNC_PART_START = '(';
    public static final char FUNC_PART_END = ')';
    public static final char STRING_PART_INDICATOR = '\'';
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeParser.class);
    private final Map<String, AbstractCodeFunction> dictionary = new HashMap<>();
    private final Map<String, String> values = new HashMap<>();

    public CodeParser() {
        this(new AbstractCodeFunction[0]);
    }

    public CodeParser(AbstractCodeFunction... codeFunctions) {
        this.addFunctions(codeFunctions);
    }

    public static Set<Class<?>> discoverCodeFunctions(Application application) {
        return discoverCodeFunctions(application.getReflections());
    }

    public static Set<Class<?>> discoverCodeFunctions(Reflections reflections) {
        return reflections.getTypesAnnotatedWith(CodeFunction.class);
    }

    public static Set<AbstractCodeFunction> createFunctionInstances(Set<Class<?>> codeFunctions) {
        return codeFunctions.stream()
                .map(codeFunction -> {
                    try {
                        return BeanUtils.getResolvableConstructor(codeFunction).newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        LOGGER.warn("Could not create function class " + codeFunction.getName() + ". Ignoring...", e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .filter(o -> o instanceof AbstractCodeFunction)
                .map(c -> (AbstractCodeFunction) c)
                .collect(Collectors.toSet());
    }

    public CodeParser addValues(Map<String, String> values) {
        this.values.putAll(values);
        return this;
    }

    public CodeParser addValue(String key, String val) {
        this.values.put(key, val);
        return this;
    }

    public CodeParser addFunctions(AbstractCodeFunction... abstractCodeFunctions) {
        for (AbstractCodeFunction abstractCodeFunction : abstractCodeFunctions) {
            abstractCodeFunction.setCodeParser(this);
            this.dictionary.put(abstractCodeFunction.getFunctionName().toLowerCase(), abstractCodeFunction);
        }

        return this;
    }

    public String parseCode(String input) {
        if (!hasParsableCode(input)) return input;

        StringBuilder result = new StringBuilder();

        int currentIndex = input.indexOf(CODE_PART_START);
        if (currentIndex > 0) result.append(input, 0, currentIndex);

        while (hasParsableCode(input, currentIndex)) {
            String code = getFunctionBody(input, currentIndex);
            if (code.equals("error")) return "error";

            String content = getContent(code, CODE_PART_START, CODE_PART_END);

            String parsed = parseFunction(content);
            if (parsed.equals("error")) return "error";
            result.append(parsed);

            currentIndex += code.length();
            int openIndex = input.indexOf(CODE_PART_START, currentIndex);
            if (openIndex == -1) break;

            result.append(input, currentIndex, openIndex);
            currentIndex = openIndex;
        }

        if (currentIndex >= 0 && currentIndex < input.length())
            result.append(input.substring(currentIndex));

        return result.toString();
    }

    public String parseFunction(String input) {
        if (!isFunction(input, FUNC_PART_START, FUNC_PART_END)) {
            if (values.containsKey(input)) return values.get(input);
            if (hasParsableCode(input)) input = parseCode(input);

            return isStringPart(input) ? getStringContent(input) : input;
        }

        String name = getFunctionName(input, FUNC_PART_START);
        if (!dictionary.containsKey(name.toLowerCase())) return "error";

        return dictionary.get(name).parse(input);
    }

    public boolean isFunction(String input, char start, char end) {
        int a = input.indexOf(start);
        int b = input.indexOf(end);

        return a != -1 && b != -1 && a < b && dictionary.containsKey(input.substring(0, a).trim().toLowerCase());
    }
}
