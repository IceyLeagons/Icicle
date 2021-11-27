package net.iceyleagons.icicle.core.translations.impl;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.core.translations.LanguageProvider;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 27, 2021
 */
@RequiredArgsConstructor
public class ConstantLanguageProvider implements LanguageProvider {

    private final String language;

    @Override
    public String getLanguage(Object key) {
        return this.language;
    }
}
