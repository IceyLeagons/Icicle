/*
 * MIT License
 *
 * Copyright (c) 2023 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.core.modifiers.impl.throwif;

import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.iceyleagons.icicle.core.exceptions.BeanCreationException;
import net.iceyleagons.icicle.core.modifiers.MethodValueModifier;
import net.iceyleagons.icicle.core.modifiers.ValueModifier;
import net.iceyleagons.icicle.core.modifiers.impl.defaultValue.DefaultValue;
import net.iceyleagons.icicle.core.utils.BeanUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Map;

/**
 * Implementation of the {@link ThrowIf} modifier.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jan. 02, 2023
 */
@MethodValueModifier(ThrowIf.class)
public class ThrowIfHandler implements ValueModifier {

    private final Map<Class<? extends ThrowIfFilterTemplate>, ThrowIfFilterTemplate> cache = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    @Override
    public Object modify(Object input, Parameter parameter) throws Exception {
        final ThrowIf annotation = parameter.getAnnotation(ThrowIf.class);
        final ThrowIfFilterTemplate filter = getOrCreateFilter(annotation.filter());

        if (!filter.filter(parameter, input)) {
            throw createException(annotation.exception());
        }

        return input;
    }

    /**
     * Creates the exception from the given type.
     *
     * @param exception the exception type
     * @return the resulting exception object
     * @throws BeanCreationException if the exception cannot be created
     */
    private Exception createException(Class<? extends Exception> exception) throws BeanCreationException {
        final Constructor<? extends Exception> constructor = BeanUtils.getResolvableConstructor(exception);
        if (constructor.getParameterCount() != 0)
            throw new IllegalStateException("Exception must have a public empty constructor if used in @ThrowIf!");

        return BeanUtils.instantiateClass(constructor, null);
    }

    /**
     * Creates the filter from the given type
     *
     * @param filterClass the filter type
     * @return the resulting filter instance
     * @throws BeanCreationException if the filter cannot be created
     */
    private ThrowIfFilterTemplate getOrCreateFilter(Class<? extends ThrowIfFilterTemplate> filterClass) throws BeanCreationException {
        if (cache.containsKey(filterClass)) {
            return cache.get(filterClass);
        }

        final Constructor<? extends ThrowIfFilterTemplate> constructor = BeanUtils.getResolvableConstructor(filterClass);
        if (constructor.getParameterCount() != 0)
            throw new IllegalStateException("Filters must have a public empty constructor!");

        return BeanUtils.instantiateClass(constructor, null);
    }
}
