/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
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

package net.iceyleagons.icicle.wrapped.bukkit;

import net.iceyleagons.icicle.reflect.Reflections;

import java.lang.reflect.Method;

/**
 * Wrapped representation of CraftChatMessage
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since 1.3.3-SNAPSHOT
 */
public class WrappedCraftChatMessage {

    private static final Class<?> cb_craftChatMessage;
    private static final Method fromString;

    static {
        cb_craftChatMessage = Reflections.getNormalCBClass("util.CraftChatMessage");
        fromString = Reflections.getMethod(cb_craftChatMessage, "fromString", true, String.class);
    }

    /**
     * Parses a string and converts it into a CraftChatMessage array.
     *
     * @param string the string we wish to convert.
     * @return an array containing CraftChatMessages.
     */
    public static Object[] fromString(String string) {
        return Reflections.invoke(fromString, Object[].class, cb_craftChatMessage, string);
    }

}
