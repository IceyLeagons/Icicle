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

package net.iceyleagons.icicle.core.translations.impl.file;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.utilities.file.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 03, 2022
 */
@Getter
@EqualsAndHashCode
public abstract class LanguageFile {

    private final Map<String, String> values = new HashMap<>();
    private final Path path;
    private final String providedLanguageCode;

    @Setter
    private boolean appendDefaults;

    public LanguageFile(File file, String providedLanguageCode, boolean appendDefaults) {
        this.path = file.toPath();
        this.providedLanguageCode = providedLanguageCode;
        this.appendDefaults = appendDefaults;

        FileUtils.createFileIfNotExists(this.path, true);
    }

    protected void readInData() {
        values.putAll(readData(this.path));
    }

    public String getString(String key, String defaultValue) {
        if (values.containsKey(key)) {
            return values.get(key);
        }

        if (appendDefaults) {
            writeDefaultToFile(this.path, key, defaultValue);
        }

        return defaultValue;
    }

    protected abstract void writeHeader(Path path);

    protected abstract void writeDefaultToFile(Path path, String key, String value);

    protected abstract Map<String, String> readData(Path path);
}
