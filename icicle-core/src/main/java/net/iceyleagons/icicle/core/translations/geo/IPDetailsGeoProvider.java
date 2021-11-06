package net.iceyleagons.icicle.core.translations.geo;

import net.iceyleagons.icicle.core.translations.LanguageProvider;
import net.iceyleagons.icicle.utilities.WebUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPDetailsGeoProvider implements LanguageProvider {

    private static final Pattern PATTERN = Pattern.compile("\"country_short\":\"(.*?)\"");
    private static final Pattern IP_PATTERN = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
    private static final String URL_PATTERN = "https://free.ipdetails.io/%ip%";

    @Override
    public String getLanguage(Object key) {
        if (IP_PATTERN.matcher(key.toString()).matches()) {
            String webResponse = WebUtils.readURL(URL_PATTERN.replaceAll("%ip%", key.toString()));
            Matcher matcher = PATTERN.matcher(webResponse);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }

        return null; //Null check should be present when calling --> use default language
    }
}