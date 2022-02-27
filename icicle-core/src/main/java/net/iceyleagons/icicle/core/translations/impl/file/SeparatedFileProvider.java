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

package net.iceyleagons.icicle.core.translations.impl.file;

import net.iceyleagons.icicle.core.translations.TranslationStringProvider;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import net.iceyleagons.icicle.utilities.file.FileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The first row of the files are ignored, this is preserved for column names in the file.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 27, 2021
 */
public abstract class SeparatedFileProvider implements TranslationStringProvider {

    private final Map<String, Map<String, String>> values = new ConcurrentHashMap<>();
    private final String delimiter;

    public SeparatedFileProvider(String delimiter, File... csvFiles) {
        this.delimiter = delimiter;
        for (File csvFile : csvFiles) {
            if (!csvFile.exists() || csvFile.isDirectory()) continue;

            addCsvFile(csvFile);
        }
    }

    public void addCsvFile(File file) {
        if (!file.exists() || file.isDirectory()) return;
        final String[] lines = ArrayUtils.ignoreFirst(1, FileUtils.getContent(file.toPath(), true).split(System.lineSeparator()));

        for (final String line : lines) {
            String[] columns = line.split(delimiter);

            String lang = columns[0];
            String key = columns[1];
            String string = columns[2];

            if (!values.containsKey(lang)) {
                values.put(lang, new HashMap<>());
            }

            values.get(lang).put(key, string);
        }
    }

    @Override
    public String get(String language, String key) {
        return values.containsKey(language) ? values.get(language).get(key) : null;
    }
}
