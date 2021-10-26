package net.iceyleagons.icicle.core.translations.geo;

import net.iceyleagons.icicle.core.translations.LanguageProvider;
import net.iceyleagons.icicle.utilities.WebUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPDetailsGeoProvider implements LanguageProvider {

    private static final Pattern PATTERN = Pattern.compile("\"country_short\":\"(.*?)\"");
    private static final String URL_PATTERN = "https://free.ipdetails.io/%ip%";

    @Override
    public String getLanguage(String key) {
        String webResponse = WebUtils.readURL(URL_PATTERN.replaceAll("%ip%", key));
        Matcher matcher = PATTERN.matcher(webResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null; //Null check should be present when calling --> use default language
    }
}