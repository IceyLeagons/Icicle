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

/**
 * Used when the {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDelegator} cannot delegate the configuration to the
 * proper {@link net.iceyleagons.icicle.core.configuration.driver.ConfigDriver}.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jul. 19, 2022
 */
public class ConfigDelegationException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param msg the error message
     */
    public ConfigDelegationException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param msg the error message
     * @param t the underlying cause of the exception
     */
    public ConfigDelegationException(String msg, Throwable t) {
        super(msg, t);
    }
}
