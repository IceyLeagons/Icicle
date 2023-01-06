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

package net.iceyleagons.icicle.core.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * This exception has special properties to make it compatible with our translation system.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
@Getter
@RequiredArgsConstructor
public class TranslatableException extends Exception {

    /**
     * The translation key
     */
    private final String key;

    /**
     * The default value of the translation
     */
    private final String defaultValue;

    /**
     * The parameters to be passed to the string code parser
     */
    private final Map<String, String> params;

    /**
     * @param key the translation key
     * @param defaultValue the default value of the translation
     */
    public TranslatableException(String key, String defaultValue) {
        this(key, defaultValue, Map.of());
    }
}
