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

package net.iceyleagons.icicle.commands;

import net.iceyleagons.icicle.commands.annotations.Alias;
import net.iceyleagons.icicle.commands.annotations.Description;
import net.iceyleagons.icicle.commands.annotations.Usage;
import net.iceyleagons.icicle.utilities.ArrayUtils;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 */
public interface Command extends CompletionHandler {

    static String[] getAliases(Method method) {
        final String[] aliases = method.isAnnotationPresent(Alias.class) ? method.getAnnotation(Alias.class).value() : new String[0];
        return Arrays.stream(aliases).map(String::toLowerCase).toArray(String[]::new);
    }

    CommandManager getManager();

    String getName();

    String[] getAliases();

    boolean isSupplyingTranslationKeys();

    Method getMethod();

    Object getOrigin();

    default Class<?>[] getParameterTypes() {
        return this.getMethod().getParameterTypes();
    }

    default String execute(Object[] args) throws Exception {
        final Object result = this.getMethod().invoke(this.getOrigin(), args);
        return result == null ? null : result.toString();
    }

    default String[] getAllCommandNames() {
        return ArrayUtils.appendToArray(this.getAliases(), this.getName());
    }

    default Usage getUsage() {
        return ReflectionUtils.getAnnotationOrNull(this.getMethod(), Usage.class);
    }

    default Description getDescription() {
        return ReflectionUtils.getAnnotationOrNull(this.getMethod(), Description.class);
    }
}
