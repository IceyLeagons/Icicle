package net.iceyleagons.icicle.core.translations;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
@Service
public class TranslationService {

    private final Application application;
    private final Set<AbstractCodeFunction> codeFunctions;
    private final TranslationStringProvider translationStringProvider = null; //TODO get from application

    public TranslationService(Application application) {
        this.application = application;
        this.codeFunctions = CodeParser.createFunctionInstances(CodeParser.discoverCodeFunctions(application));
    }

    public String getTranslation(String key, String language, String defaultValue) {
        return getTranslation(key, language, defaultValue, Collections.emptyMap());
    }

    public String getTranslation(String key, String language, String defaultValue, Map<String, String> values) {
        if (translationStringProvider == null) return "NO TRANSLATION PROVIDER SET UP!";

        String translation = translationStringProvider.get(key, language);
        String toParse = translation == null ? defaultValue : translation;

        return getNewParser().addValues(values).parseCode(toParse);
    }

    private CodeParser getNewParser() {
        return new CodeParser(codeFunctions.toArray(AbstractCodeFunction[]::new));
    }
}
