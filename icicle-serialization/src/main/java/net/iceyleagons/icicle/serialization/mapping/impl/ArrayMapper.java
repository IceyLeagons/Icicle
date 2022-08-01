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
import java.lang.reflect.Array;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
@SerializationPropertyMapper
public class ArrayMapper extends PropertyMapper<Object> {
    static Object[] mapArray(Object genericArray, ObjectMapper context) {
        final Object[] array = GenericUtils.genericArrayToNormalArray(genericArray, Object.class);

        final Object[] clone = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
            final Object obj = array[i];

            if (!SerializationUtils.isSubObject(obj.getClass())) {
                clone[i] = array[i];
                continue;
            }

            clone[i] = context.mapObject(array[i]);
        }

        return clone;
    }

    static Object deMapArray(Object array, Class<?> arrayType, ObjectMapper context) {
        final int length = Array.getLength(array);
        final Object clone = GenericUtils.createGenericArrayWithoutCasting(arrayType, length);

        for (int i = 0; i < length; i++) {
            final Object obj = Array.get(array, i);

            if (!SerializationUtils.isSubObject(obj.getClass())) {
                Array.set(clone, i, obj);
                continue;
            }

            if (!(obj instanceof MappedObject)) {
                throw new IllegalStateException("Got non MappedObject!");
            }

            Array.set(clone, i, context.demapObject((MappedObject) Array.get(array, i), arrayType));
        }

        return clone;
    }

    @Override
    public Object deMap(Object object, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        return deMapArray(GenericUtils.genericArrayToNormalArray(object, Object.class), originalType.getComponentType(), context);
    }

    @Override
    public ObjectValue mapCasted(Object object, Class<?> javaType, ObjectMapper context, ObjectValue old) {
        return old.copyWithNewValueAndType(mapArray(object, context), javaType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SerializationUtils.isArray(type) && !SerializationUtils.isCollection(type);
    }
}
