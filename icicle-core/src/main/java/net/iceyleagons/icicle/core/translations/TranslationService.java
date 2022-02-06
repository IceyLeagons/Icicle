/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.core.translations;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.core.Application;
import net.iceyleagons.icicle.core.annotations.Service;
import net.iceyleagons.icicle.core.translations.code.CodeParser;
import net.iceyleagons.icicle.core.translations.code.functions.AbstractCodeFunction;
import net.iceyleagons.icicle.core.translations.impl.ConstantLanguageProvider;
import org.reflections.Reflections;

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
        String translation = translationStringProvider == null ? defaultValue : translationStringProvider.get(key, language);
        String toParse = translation == null ? defaultValue : translation;

        return getNewParser().addValues(values).parseCode(toParse);
    }

    private CodeParser getNewParser() {
        return new CodeParser(codeFunctions.toArray(AbstractCodeFunction[]::new));
    }
}
