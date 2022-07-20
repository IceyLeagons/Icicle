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
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.SerializationPropertyMapper;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
@SerializationPropertyMapper
public class CollectionMapper extends PropertyMapper<Collection<?>> {

    @Override
    public Collection<?> deMap(Object genericArray, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations, ObjectValue objectValue) {
        final Object[] array = GenericUtils.genericArrayToNormalArray(genericArray, Object.class);

        if (!SerializationUtils.isSubObject(array.getClass().getComponentType()))
            return toCollection(originalType, array);
        return toCollection(originalType, ArrayMapper.deMapArray(array, objectValue.getGenericGetter().getGenericClass(0), context));
    }

    @Override
    public ObjectValue mapCasted(Collection<?> object, Class<?> javaType, ObjectMapper context, ObjectValue old) {
        final Object[] array = object.toArray();
        if (!SerializationUtils.isSubObject(array.getClass().getComponentType()))
            return old.copyWithNewValueAndType(array, javaType); // ArrayMapper.arrayToMappedObject(array, key, javaType, annotations, setter, getter);

        return old.copyWithNewValueAndType(ArrayMapper.mapArray(array, context), javaType); // ArrayMapper.arrayToMappedObject(ArrayMapper.mapArray(array, context), key, javaType, annotations, setter, getter);
    }


    @Override
    public boolean supports(Class<?> type) {
        return SerializationUtils.isCollection(type);
    }

    private static Collection<?> toCollection(Class<?> type, Object array) {
        final Collection<Object> collection = (Collection<Object>) SerializationUtils.createCollectionFromType(type);

        for (int i = 0; i < Array.getLength(array); i++) {
            collection.add(Array.get(array, i));
        }

        return collection;
    }
}
