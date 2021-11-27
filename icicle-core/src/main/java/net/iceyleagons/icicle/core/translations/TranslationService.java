package net.iceyleagons.icicle.core.translations;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.impl.ConstantLanguageProvider;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Getter
@Service
public class TranslationService {

    private final Application application;
    private final Set<AbstractCodeFunction> codeFunctions;

    @Setter
    private TranslationStringProvider translationStringProvider; // will require setting up with a setter from the application

    @Setter
    private LanguageProvider languageProvider = new ConstantLanguageProvider("en"); // will require setting up with a setter from the application

    public TranslationService(Application application) {
        this.application = application;
        this.codeFunctions = CodeParser.createFunctionInstances(CodeParser.discoverCodeFunctions(application));
    }

    public String getTranslation(String key, String language, String defaultValue) {
        return getTranslation(key, language, defaultValue, Collections.emptyMap());
    }

    public String getTranslation(String key, String language, String defaultValue, Map<String, String> values) {
        if (translationStringProvider == null) return defaultValue;

        String translation = translationStringProvider.get(key, language);
        String toParse = translation == null ? defaultValue : translation;

        return getNewParser().addValues(values).parseCode(toParse);
    }

    private CodeParser getNewParser() {
        return new CodeParser(codeFunctions.toArray(AbstractCodeFunction[]::new));
    }
}
