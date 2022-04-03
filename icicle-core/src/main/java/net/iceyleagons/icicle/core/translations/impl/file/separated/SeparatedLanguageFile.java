/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.translations.impl.file.separated;

import net.iceyleagons.icicle.core.translations.impl.file.LanguageFile;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import net.iceyleagons.icicle.utilities.StringUtils;
import net.iceyleagons.icicle.utilities.file.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 03, 2022
 */
public abstract class SeparatedLanguageFile extends LanguageFile {

    private final String delimiter;

    public SeparatedLanguageFile(File file, String providedLanguageCode, boolean appendDefaults, String delimiter) {
        super(file, providedLanguageCode, appendDefaults);
        this.delimiter = delimiter;
        super.readInData();
    }

    @Override
    protected void writeHeader(Path path) {
        FileUtils.appendFile(path, String.join(this.delimiter, "Key", "English String", "Translated String"));
    }

    @Override
    protected void writeDefaultToFile(Path path, String key, String value) {
        FileUtils.appendFile(path, String.join(this.delimiter, key, value, value));
    }

    @Override
    protected Map<String, String> readData(Path path) {
        String content = FileUtils.getContent(path, true);
        if (content.isEmpty()) {
            writeHeader(path);
        }

        String[] rawLines = content.split(System.lineSeparator());

        Map<String, String> values = new HashMap<>();

        final String[] lines = ArrayUtils.ignoreFirst(1, rawLines);
        for (String line : lines) {
            String[] data = line.split(this.delimiter);

            String key = StringUtils.nullToEmpty(data[0]);
            if (key.isEmpty()) continue;

            String english = StringUtils.nullToEmpty(data[1]);
            String value = StringUtils.nullToEmpty(data[2]);
            if (value.isEmpty()) value = english;

            values.put(key, value);
        }

        return values;
    }
}
