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

package net.iceyleagons.icicle.serialization.mapping.impl;

import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.dto.MappedObject;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.SerializationPropertyMapper;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.annotation.Annotation;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
@SerializationPropertyMapper
public class MapMapper extends PropertyMapper<Map<?, ?>> {

    @Override
    public Map<?, ?> deMap(Object object, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        final Object[] array = GenericUtils.genericArrayToNormalArray(object, Object.class);
        final Map<Object, Object> result = (Map<Object, Object>) SerializationUtils.createMapFromType(originalType);

        for (Object o : array) {
            if (!(o instanceof Map.Entry)) {
                throw new IllegalStateException("Got non Map.Entry value");
            }

            final Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) o;
            final Object value = entry.getValue();

            if (!SerializationUtils.isSubObject(value.getClass())) {
                result.put(entry.getKey(), value);
                continue;
            }

            if (!(value instanceof MappedObject)) {
                throw new IllegalStateException("Got non MappedObject!");
            }

            result.put(entry.getKey(), context.demapObject((MappedObject) value, Object.class));
        }

        return result;
    }

    @Override
    public ObjectValue mapCasted(Map<?, ?> map, String key, Class<?> javaType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        final Set<Map.Entry<?, ?>> entries = new HashSet<>(map.size());

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            final Object value = entry.getValue();

            if (!SerializationUtils.isValuePrimitiveOrString(entry.getKey().getClass())) {
                throw new IllegalStateException("Maps can only have a key of a string or a primitive type object representation. Consider using converters.");
            }

            if (!SerializationUtils.isSubObject(value.getClass())) {
                entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), value));
                continue;
            }

            entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), context.mapObject(value)));
        }

        return ArrayMapper.arrayToMappedObject(entries.toArray(Map.Entry<?, ?>[]::new), key, javaType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SerializationUtils.isMap(type);
    }
}
