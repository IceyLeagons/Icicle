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

import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.serialization.dto.MappedObject;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.SerializationProvider;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public class ObjectMapper {

    private final SerializationProvider serializationProvider;

    public ObjectMapper() {
        this.serializationProvider = new JsonSerializer();
    }

    public ObjectMapper(SerializationProvider serializationProvider) {
        this.serializationProvider = serializationProvider;
    }

    public MappedObject mapObject(Object object) {
        // TODO check if object is not a class, but rather a direct subobject like Map or list etc.

        if (object == null) return null;

        final Class<?> clazz = object.getClass();
        final Set<ObjectValue> values = new HashSet<>();

        for (Field declaredField : clazz.getDeclaredFields()) {
            // check ignorance

            final String name = SerializationUtils.getCustomNameOrDefault(declaredField, declaredField.getName());
            final Map<Class<? extends Annotation>, Annotation> annotations = Arrays.stream(declaredField.getAnnotations()).collect(Collectors.toMap(Annotation::annotationType, a -> a));

            mapObjectProperty(ReflectionUtils.get(declaredField, object, Object.class), name, declaredField.getType(), annotations, values);
        }

        return new MappedObject(clazz, values);
    }

    private void mapObjectProperty(Object value, String key, Class<?> javaType, Map<Class<? extends Annotation>, Annotation> annotations, Set<ObjectValue> values) {
        // TODO: Convert
        boolean converted = false;
        if (annotations.containsKey(Convert.class)) {
            Class<?> conv = ((Convert) annotations.get(Convert.class)).converter();
            for (ValueConverter<?,?> converter : ConverterAnnotationHandler.REGISTERED_CONVERTERS) {
                if (converter.supports(javaType) && conv.equals(converter.getClass())) {
                    Object result = converter.convert(value, true);

                    if (SerializationUtils.isValuePrimitiveOrString(result.getClass())) {
                        values.add(new ObjectValue(result.getClass(), key, result));
                        return;
                    }

                    value = result;
                    javaType = result.getClass();
                    converted = true;
                }
            }
            if (!converted) {
                throw new IllegalStateException("Converter not found. Converter required: " + conv);
            }
        }

        if (SerializationUtils.isValuePrimitiveOrString(javaType) && !converted) {
            values.add(new ObjectValue(javaType, key, value));
            return;
        }

        for (PropertyMapper<?> registeredPropertyMapper : PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS) {
            if (registeredPropertyMapper.supports(javaType)) {
                values.add(registeredPropertyMapper.map(value, key, javaType, this, annotations));
                return;
            }
        }

        if (SerializationUtils.isSubObject(javaType)) {
            values.add(new ObjectValue(javaType, key, mapObject(value)));
        }
    }

    public <T> T demapObject(MappedObject mappedObject, Class<T> wantedType) {
        return null;
    }

    public String writeValueAsString(Object object) {
        return serializationProvider.writeAsString(mapObject(object));
    }

    public void writeValueToFile(Object object, Path file) {
        serializationProvider.writeToFile(mapObject(object), file);
    }

    public <T> T readValueFromString(String string, Class<T> type) {
        return demapObject(serializationProvider.readFromString(string), type);
    }

    public <T> T readValueFromFile(Path file, Class<T> type) {
        return demapObject(serializationProvider.readFromFile(file), type);
    }
}
