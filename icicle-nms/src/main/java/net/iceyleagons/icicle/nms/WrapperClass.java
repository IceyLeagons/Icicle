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

import lombok.Getter;
import net.iceyleagons.icicle.utilities.AdvancedClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
public abstract class WrapperClass {

    private static final Map<String, AdvancedClass<?>> cache = new ConcurrentHashMap<>();
    private final AdvancedClass<?> clazz;
    private final Object origin;

    public WrapperClass(String name, WrapType wrapType, Object origin) {
        if (!cache.containsKey(name)) {
            cache.put(name, wrapType.getClazzProvider().get(name));
        }

        this.clazz = cache.get(name);
        this.origin = origin;
    }

    protected static AdvancedClass<?> getExtraClass(String name, WrapType wrapType) {
        if (!cache.containsKey(name)) {
            cache.put(name, wrapType.getClazzProvider().get(name));
        }

        return cache.get(name);
    }

    protected void preDiscoverMethod(String as, String name, Class<?>... paramTypes) {
        clazz.preDiscoverMethod(as, name, paramTypes);
    }

    protected <A> A executeMethod(String name, Class<A> returnType, Object... params) {
        return clazz.executeMethod(name, origin, returnType, params);
    }

    protected <A> A getFieldValue(String name, Class<A> wantedType) {
        return clazz.getFieldValue(name, origin, wantedType);
    }

    protected void setFieldValue(String name, Object value) {
        clazz.setFieldValue(name, origin, value);
    }
}
