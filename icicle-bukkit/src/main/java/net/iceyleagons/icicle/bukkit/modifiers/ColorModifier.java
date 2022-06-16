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

package net.iceyleagons.icicle.bukkit.modifiers;

import net.iceyleagons.icicle.core.modifiers.MethodValueModifier;
import net.iceyleagons.icicle.core.modifiers.ValueModifier;
import org.bukkit.ChatColor;

import java.lang.reflect.Parameter;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 16, 2022
 */
@MethodValueModifier(ParseColors.class)
public class ColorModifier implements ValueModifier {

    @Override
    public Object modify(Object input, Parameter parameter) {
        if (!parameter.getType().equals(String.class)) {
            throw new IllegalStateException("@ParseColors can only be used on String.");
        }

        return ChatColor.translateAlternateColorCodes(parameter.getAnnotation(ParseColors.class).value(), (String) input);
    }
}
