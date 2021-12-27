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

package net.iceyleagons.icicle.nms;

import net.iceyleagons.icicle.nms.annotations.CraftWrap;
import net.iceyleagons.icicle.nms.annotations.NMSWrap;
import net.iceyleagons.icicle.nms.annotations.Wrapping;
import net.iceyleagons.icicle.nms.utils.ClassHelper;
import net.iceyleagons.icicle.utilities.AdvancedClass;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 27, 2021
 */
public final class NMSHelper {

    public static String getKeyForMapping(Wrapping wrapping) {
        if (wrapping.isField()) {
            return wrapping.value();
        }

        Class<?>[] paramTypes = wrapping.paramTypes();
        return paramTypes.length == 0 ? wrapping.value() : wrapping.value() + paramTypes.length;
    }

    public static AdvancedClass<?> getWrapClass(Class<?> toWrap) {
        AdvancedClass<?> clazz = null;
        if (toWrap.isAnnotationPresent(NMSWrap.class)) {
            clazz = ClassHelper.from(toWrap.getAnnotation(NMSWrap.class));
        } else if (toWrap.isAnnotationPresent(CraftWrap.class)) {
            clazz = ClassHelper.from(toWrap.getAnnotation(CraftWrap.class));
        }

        return clazz;
    }
}
