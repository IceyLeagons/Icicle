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

package net.iceyleagons.icicle.utilities;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.utilities.exceptions.UnImplementedException;
import net.iceyleagons.icicle.utilities.lang.Utility;
import org.jetbrains.annotations.Contract;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Methods meant for static imports. These methods are inspired by Kotlin.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Apr. 16, 2022
 */
@Utility
public class ErrorUtils {

    @Contract("_ -> fail")
    public static void TODO(String msg) {
        throw new UnImplementedException(msg);
    }

    @Contract("_ -> fail")
    public static void UNSUPPORTED(String msg) {
        throw new UnsupportedOperationException(msg);
    }

    @Contract("_ -> fail")
    public static void ILLEGAL(String msg) {
        throw new IllegalStateException(msg);
    }

    @SneakyThrows
    public static String stackTraceToString(Throwable t) {
        try (StringWriter sw = new StringWriter()) {
            try (PrintWriter pw = new PrintWriter(sw)) {
                t.printStackTrace(pw);
            }

            return sw.toString();
        }
    }
}
