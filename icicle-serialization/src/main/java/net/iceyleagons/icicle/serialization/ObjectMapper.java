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

package net.iceyleagons.icicle.serialization;


import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.serialization.annotations.Convert;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Feb. 26, 2022
 */
public class ObjectMapper {

    private final Map<Class<?>, ValueConverter<?, ?>> converters = new HashMap<>();

    @SneakyThrows
    public <T> T demapObject(SerializedObject serializedObject, Class<T> wantedType) {
        final Class<?> type = serializedObject.getJavaType();
        if (!wantedType.isAssignableFrom(type))
            throw new IllegalArgumentException("Wanted type is not assignable from the mapped object!");

        Constructor<?> constructor = BeanUtils.getResolvableConstructor(type);
        if (constructor.getParameterTypes().length != 0)
            throw new IllegalStateException("Object must have an empty constructor!");

        Object object = BeanUtils.instantiateClass(constructor, null);

        for (ObjectValue value : serializedObject.getValues()) {
            if (value.shouldConvert()) {
                Object converted = convert(value.getValue(), value.getField(), false);
                ReflectionUtils.set(value.getField(), object, converted);
            } else if (value.isValuePrimitiveOrString()) {
                ReflectionUtils.set(value.getField(), object, value.getValue());
            } else if (value.isArray() && !value.isCollection()) {
                Object demapped = demapArray(GenericUtils.genericArrayToNormalArray(value.getValue(), Object.class), value.getJavaType().getComponentType());
                ReflectionUtils.set(value.getField(), object, demapped);
            } else if (value.isCollection()) {
                Object demapped = demapArray(GenericUtils.genericArrayToNormalArray(value.getValue(), Object.class), Object.class);

                Collection<Object> collection = (Collection<Object>) SerializationUtils.createCollectionFromType(value.getJavaType());
                collection.addAll(Arrays.asList(GenericUtils.genericArrayToNormalArray(demapped, Object.class)));
                ReflectionUtils.set(value.getField(), object, collection);
            } else if (value.isMap()) {
                Object demapped = demapMap(GenericUtils.genericArrayToNormalArray(value.getValue(), Object.class), value.getJavaType());
            } else if (value.isSubObject()) {
                Object demapped = demapObject((SerializedObject) value.getValue(), Object.class);
                ReflectionUtils.set(value.getField(), object, demapped);
            }
        }

        return wantedType.cast(object);
    }

    public SerializedObject mapObject(Object object) {
        if (object == null) return null;

        Class<?> clazz = object.getClass();
        Set<ObjectValue> values = SerializationUtils.getValuesForClass(clazz, (f, k) -> ReflectionUtils.get(f, object, Object.class));

        for (ObjectValue value : values) {
            if (value.isValuePrimitiveOrString()) continue;

            if (value.shouldConvert()) {
                Object converted = convert(value.getValue(), value.getField(), true);
                value.setValue(SerializationUtils.isValuePrimitiveOrString(converted.getClass()) ? converted : mapObject(converted));
            } else if (value.isArray() && !value.isCollection()) {
                value.setValue(mapArray(GenericUtils.genericArrayToNormalArray(value.getValue(), Object.class)));
            } else if (value.isCollection()) {
                Object[] collection = value.getValueAs(Collection.class).toArray();
                value.setValue(mapArray(collection));
            } else if (value.isMap()) {
                Map<?, ?> map = value.getValueAs(Map.class);
                value.setValue(mapMap(map));
            } else if (value.isSubObject()) {
                value.setValue(mapObject(value.getValue()));
            }
        }

        SerializedObject obj = new SerializedObject(clazz);
        obj.getValues().addAll(values);

        return obj;
    }

    private Object demapMap(Object[] array, Class<?> type) {
        Map<Object, Object> map = (Map<Object, Object>) SerializationUtils.createMapFromType(type);
        for (Object o : array) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) o;
            if (!SerializationUtils.isSubObject(entry.getValue().getClass())) {
                map.put(entry.getKey(), entry.getValue());
                continue;
            }

            map.put(entry.getKey(), demapObject((SerializedObject) entry.getKey(), Object.class));
        }

        return map;
    }

    private Object[] mapMap(Map<?, ?> map) {
        Set<Map.Entry<?, ?>> entries = new HashSet<>(map.size());

        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (!SerializationUtils.isValuePrimitiveOrString(entry.getKey().getClass())) {
                throw new IllegalStateException("Maps can only have a key of a string or a primitive type object representation. Consider using converters.");
            }

            if (!SerializationUtils.isSubObject(value.getClass())) {
                entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), value));
                continue;
            }

            entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), mapObject(value)));
        }

        return entries.toArray(Map.Entry<?, ?>[]::new);
    }

    private Object[] mapArray(Object[] original) {
        if (!SerializationUtils.isSubObject(original.getClass().getComponentType())) return original;

        Object[] clone = new Object[original.length];
        for (int i = 0; i < original.length; i++) {
            if (!SerializationUtils.isSubObject(original[i].getClass())) {
                clone[i] = original[i];
                continue;
            }

            clone[i] = mapObject(original[i]);
        }

        return clone;
    }

    private Object demapArray(Object[] mapped, Class<?> originalType) {
        if (!SerializationUtils.isSubObject(mapped.getClass().getComponentType())) return mapped;

        Object clone = GenericUtils.createGenericArrayWithoutCasting(originalType, mapped.length);
        for (int i = 0; i < mapped.length; i++) {
            if (!SerializationUtils.isSubObject(mapped[i].getClass())) {
                Array.set(clone, i, mapped[i]);
                continue;
            }

            Array.set(clone, i, demapObject((SerializedObject) mapped[i], originalType));
        }

        return clone;
    }

    private Object convert(Object input, Field field, boolean serialize) {
        Class<?> converterClass = field.getAnnotation(Convert.class).value();

        if (!converters.containsKey(converterClass)) {
            try {
                // We could use the core to handle the Converters for us via @AnnotationHandlers (auto-creation), but
                // I think it's much better to have Converters isolated per ObjectMapper, so we create them here, but one caveat
                // is that you cannot autowire them.
                // Maybe???? a TODO: implement it via @AutoCreate (p.s: tried but there's a null pointer and I don't have time to figure it out, so will do it later down the line)
                Object converter = BeanUtils.getResolvableConstructor(converterClass).newInstance();
                if (!(converter instanceof ValueConverter)) {
                    throw new IllegalStateException(converterClass.getName() + " does not implement ValueConverter (extend AbstractConverter)!");
                }

                converters.put(converterClass, (ValueConverter<?, ?>) converter);
            } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
                throw new IllegalStateException("Could not create Converter " + converterClass.getName());
            }
        }

        ValueConverter<Object, Object> converter = (ValueConverter<Object, Object>) converters.get(converterClass);

        try {
            return serialize ? converter.serialize(input) : converter.fromSerialized(input);
        } catch (Exception e) {
            throw new IllegalStateException("Could not convert field.", e);
        }
    }
}
