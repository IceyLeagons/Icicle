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