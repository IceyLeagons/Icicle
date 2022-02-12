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

package net.iceyleagons.icicle.serialization;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.serialization.annotations.Convert;
import net.iceyleagons.icicle.serialization.annotations.SerializeIgnore;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
@SuppressWarnings("unchecked")
public class ObjectMapper {

    private Map<Class<?>, ValueConverter<?, ?>> converters = new HashMap<>();

    public static boolean shouldConvert(Field field) {
        return field.isAnnotationPresent(Convert.class);
    }

    public static boolean isSupportedCollection(Class<?> t) {
        return t.equals(List.class) || t.equals(Collection.class) || t.equals(ArrayList.class) || t.equals(Set.class) || t.equals(HashSet.class);
    }

    public static Map<Object, Object> createMapFromType(Class<?> t) {
        if (t.equals(HashMap.class) || t.equals(Map.class)) {
            return new HashMap<>();
        } else if (t.equals(ConcurrentHashMap.class)) {
            return new ConcurrentHashMap<>();
        } else {
            throw new IllegalStateException("Unsupported type for map (deserialization): " + t.getName());
        }
    }

    public static Object createCollectionFromType(Class<?> t) {
        if (t.equals(List.class) || t.equals(Collection.class) || t.equals(ArrayList.class)) {
            return new ArrayList<>();
        } else if (t.equals(Set.class) || t.equals(HashSet.class)) {
            return new HashSet<>();
        } else {
            throw new IllegalStateException("Unsupported type for collection (deserialization): " + t.getName());
        }
    }

    public static boolean shouldIgnore(Field field) {
        return Modifier.isTransient(field.getModifiers()) || field.isAnnotationPresent(SerializeIgnore.class);
    }

    public static String getName(Field field) {
        return field.isAnnotationPresent(SerializedName.class) ? field.getAnnotation(SerializedName.class).value() : field.getName().toLowerCase();
    }

    @SneakyThrows
    protected <T> T demapObject(MappedObject mappedObject, Class<T> wantedType) {
        final Class<?> type = mappedObject.getJavaType();
        if (!wantedType.isAssignableFrom(type))
            throw new IllegalArgumentException("Wanted type is not assignable from the mapped object!");

        Constructor<?> constructor = BeanUtils.getResolvableConstructor(type);
        if (constructor.getParameterTypes().length != 0)
            throw new IllegalStateException("Object must have an empty constructor!");

        Object object = constructor.newInstance();

        for (ObjectValue value : mappedObject.getValues()) {
            Field field = value.getField();
            if (shouldIgnore(field)) continue;

            if (value.isValuePrimitiveOrString() || (value.isArray() && !value.isCollection())) {
                ReflectionUtils.set(field, object, value.getValue());
            } else if (shouldConvert(field)) {
                Object converted = convert(ReflectionUtils.get(field, value.getValue(), Object.class), field, false);
                if (ObjectValue.isValuePrimitiveOrString(converted.getClass())) {
                    ReflectionUtils.set(field, object, converted);
                } else {
                    ReflectionUtils.set(field, object, demapObject((MappedObject) converted, field.getType()));
                }
            } else if (value.isCollection()) {
                Object mappedArray = value.getValue();
                Class<?> t = value.getJavaType();

                int arraySize = Array.getLength(mappedArray);
                Collection<Object> collection = (Collection<Object>) createCollectionFromType(t);


                for (int i = 0; i < arraySize; i++) {
                    Object demapped = demapObject((MappedObject) Array.get(mappedArray, i), Object.class);
                    collection.add(demapped);
                }

                ReflectionUtils.set(field, object, collection);
            } else if (value.isMap()) {
                //TODO figure out
            } else if (value.isSubObject()) {
                Object demapped = demapObject((MappedObject) value.getValue(), Object.class);
                ReflectionUtils.set(field, object, demapped);
            }
        }

        return wantedType.cast(object);
    }

    protected MappedObject mapObject(Object object) {
        final Class<?> type = object.getClass();
        final MappedObject mappedObject = new MappedObject(type);

        for (Field declaredField : type.getDeclaredFields()) {
            if (shouldIgnore(declaredField)) continue;

            final Class<?> fieldType = declaredField.getType();
            ObjectValue result;

            if (ObjectValue.isValuePrimitiveOrString(fieldType) || (ObjectValue.isArray(fieldType) && !ObjectValue.isCollection(fieldType))) {
                result = new ObjectValue(fieldType, declaredField, ReflectionUtils.get(declaredField, object, Object.class));
            } else if (shouldConvert(declaredField)) {
                Object converted = convert(ReflectionUtils.get(declaredField, object, Object.class), declaredField, true);
                if (ObjectValue.isValuePrimitiveOrString(converted.getClass())) {
                    result = new ObjectValue(converted.getClass(), declaredField, converted);
                } else {
                    result = new ObjectValue(MappedObject.class, declaredField, mapObject(converted));
                }
            } else if (ObjectValue.isCollection(fieldType)) {
                Object[] collection = Objects.requireNonNull(ReflectionUtils.get(declaredField, object, Collection.class)).toArray();
                Object[] collArray = new Object[collection.length];

                for (int i = 0; i < collection.length; i++) {
                    Object colValue = collection[i];
                    collArray[i] = mapObject(colValue);
                }

                result = new ObjectValue(fieldType, declaredField, collArray);
            } else if (ObjectValue.isMap(fieldType)) {
                Map<?, ?> map = ReflectionUtils.get(declaredField, object, Map.class);

                assert map != null;
                Set<Map.Entry<?, ?>> values = new HashSet<>(map.size());

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object value = entry.getValue();
                    if (!ObjectValue.isValuePrimitiveOrString(entry.getKey().getClass())) {
                        throw new IllegalStateException("Maps can only have a key of a string or a primitive type object representation. Consider using converters.");
                    }


                    if (ObjectValue.isValuePrimitiveOrString(value.getClass())) {
                        values.add(new AbstractMap.SimpleEntry<>(entry.getKey(), value));
                        continue;
                    }

                    values.add(new AbstractMap.SimpleEntry<>(entry.getKey(), mapObject(value)));
                }

                result = new ObjectValue(fieldType, declaredField, values.toArray(Map.Entry<?, ?>[]::new));
            } else if (ObjectValue.isSubObject(fieldType)) {
                Object value = ReflectionUtils.get(declaredField, object, Object.class);
                result = new ObjectValue(fieldType, declaredField, value != null ? mapObject(value) : null);
            } else {
                throw new IllegalStateException("Unsupported value type: " + fieldType.getName());
            }

            mappedObject.addValue(result);
        }

        return mappedObject;
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