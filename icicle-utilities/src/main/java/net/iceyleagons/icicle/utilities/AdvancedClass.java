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

package net.iceyleagons.icicle.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Dec. 23, 2021
 */
@Getter
@RequiredArgsConstructor
public class AdvancedClass<T> {

    private final Class<T> clazz;
    private final Map<String, Field> fieldCache = new ConcurrentHashMap<>();
    private final Map<String, Method> methodCache = new ConcurrentHashMap<>();

    public Field getField(String name) {
        if (fieldCache.containsKey(name)) {
            return fieldCache.get(name);
        }

        Field field = ReflectionUtils.getField(clazz, name, true);
        fieldCache.put(name, field);
        return field;
    }

    public void preDiscoverMethod(String as, String name, Class<?>... paramTypes) {
        if (methodCache.containsKey(as)) return;
        methodCache.put(as, ReflectionUtils.getMethod(clazz, name, true, paramTypes));
    }

    public Method getMethod(String name, Class<?>... paramTypes) {
        if (methodCache.containsKey(name)) {
            return methodCache.get(name);
        }

        Method method = ReflectionUtils.getMethod(clazz, name, true, paramTypes);
        methodCache.put(name, method);
        return method;
    }

    public <A> A executeMethod(String name, Object parent, Class<A> returnType, Object... params) {
        return ReflectionUtils.execute(getMethod(name), parent, returnType, params);
    }

    public <A> A getFieldValue(String name, Object parent, Class<A> wantedType) {
        return ReflectionUtils.get(getField(name), parent, wantedType);
    }

    public void setFieldValue(String name, Object parent, Object value) {
        ReflectionUtils.set(getField(name), parent, value);
    }
}
