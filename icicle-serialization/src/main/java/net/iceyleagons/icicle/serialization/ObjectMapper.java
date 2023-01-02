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
import net.iceyleagons.icicle.serialization.annotations.SerializeIgnore;
import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.ConverterAnnotationHandler;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.serialization.dto.MappedObject;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapperAnnotationHandler;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;
import net.iceyleagons.icicle.serialization.serializers.SerializationProvider;
import net.iceyleagons.icicle.utilities.Defaults;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.1.0
 * @since Jun. 13, 2022
 */
public class ObjectMapper {

    public static boolean USE_DATA_VER = false;
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
        final int version = SerializationUtils.getVersion(clazz);

        final Set<ObjectValue> values = SerializationUtils.getObjectValues(clazz, version)
                .stream()
                .map(o -> mapObjectProperty(o, object))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        return new MappedObject(clazz, values, version); // negative 1 means no information found
    }

    private ObjectValue mapObjectProperty(ObjectValue objectValue, Object parent) {
        Object value = objectValue.getGetter().get(parent);
        if (objectValue.getAnnotations().containsKey(SerializeIgnore.class)) {
            IgnoreCondition condition = ((SerializeIgnore) objectValue.getAnnotations().get(SerializeIgnore.class)).value();
            if (condition == IgnoreCondition.NO_CONDITION) {
                return null; //objectValue.copyWithNewValueAndType(null, objectValue.getJavaType());
            }
            if (condition == IgnoreCondition.IF_NULL && value == null) {
                return null; //objectValue.copyWithNewValueAndType(null, objectValue.getJavaType());
            }
        }
        Class<?> javaType = objectValue.getJavaType();
        boolean converted = false;

        if (objectValue.shouldConvert()) {
            Class<?> conv = ((Convert) objectValue.getAnnotations().get(Convert.class)).converter();
            for (ValueConverter<?, ?> converter : ConverterAnnotationHandler.REGISTERED_CONVERTERS) {
                if (converter.supports(javaType) && conv.equals(converter.getClass())) {
                    final Object result = converter.convert(value, true);
                    final Class<?> resultClass = result.getClass();

                    if (SerializationUtils.isValuePrimitiveOrString(resultClass)) {
                        return objectValue.copyWithNewValueAndType(result, resultClass);
                    }

                    value = result;
                    javaType = resultClass;
                    converted = true;
                }
            }
            if (!converted) {
                throw new IllegalStateException("Converter not found. Converter required: " + conv);
            }
        }

        if (SerializationUtils.isValuePrimitiveOrString(javaType) && !converted) {
            return objectValue.copyWithNewValueAndType(value, javaType);
        }

        for (PropertyMapper<?> registeredPropertyMapper : PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS) {
            if (registeredPropertyMapper.supports(javaType)) {
                return registeredPropertyMapper.map(value, javaType, this, objectValue);
            }
        }

        if (SerializationUtils.isSubObject(javaType)) {
            return objectValue.copyWithNewValueAndType(mapObject(value), javaType);
        }

        return null;
    }

    @SneakyThrows
    public <T> T demapObject(MappedObject mappedObject, Class<T> wantedType) {
        final Class<?> type = mappedObject.getJavaType();
        if (!wantedType.isAssignableFrom(type)) {
            throw new IllegalArgumentException("Wanted type is not assignable from actual mapped object type");
        }

        final Constructor<?> constructor = BeanUtils.getResolvableConstructor(type);
        if (constructor.getParameterTypes().length != 0) {
            throw new IllegalStateException("Object must have a public empty constructor!");
        }

        final Object object = BeanUtils.instantiateClass(constructor, null);

        for (ObjectValue value : mappedObject.getValues()) {
            demapObjectProperty(value, object, mappedObject.getVersion());
        }

        return wantedType.cast(object);
    }

    private void demapObjectProperty(final ObjectValue objectValue, final Object parent, final int dataVersion) {
        // dataVersion is the read in information

        Class<?> javaType = objectValue.getJavaType();
        Object value = objectValue.getValue();
        if (objectValue.getAnnotations().containsKey(SerializeIgnore.class)) {
            IgnoreCondition condition = ((SerializeIgnore) objectValue.getAnnotations().get(SerializeIgnore.class)).value();
            if (condition == IgnoreCondition.NO_CONDITION) {
                return;
            }
            if (condition == IgnoreCondition.IF_NULL && value == null) {
                return;
            }
        }
        if (objectValue.isVersioned() && USE_DATA_VER) {
            if (dataVersion == -1) {
                throw new IllegalStateException("Object value is versioned, but no version information was found!");
            }

            final int currentVersion = objectValue.getVersion(); // this is coming from local fields --> currentVersion

            if (dataVersion < currentVersion) {
                // data was coming from an older version, we need to set to default
                Object defVal = Defaults.DEFAULT_TYPE_VALUES.getOrDefault(objectValue.getJavaType(), null);
                objectValue.getSetter().accept(parent, defVal);
                return;
            }
        }

        boolean converted = false;

        if (objectValue.shouldConvert()) {
            Class<?> conv = ((Convert) objectValue.getAnnotations().get(Convert.class)).converter();
            for (ValueConverter<?, ?> converter : ConverterAnnotationHandler.REGISTERED_CONVERTERS) {
                if (converter.supports(javaType) && conv.equals(converter.getClass())) {
                    final Object result = converter.convert(value, false);
                    final Class<?> resultClass = result.getClass();

                    if (SerializationUtils.isValuePrimitiveOrString(resultClass)) {
                        objectValue.getSetter().accept(parent, result);
                        return;
                    }

                    value = result;
                    javaType = resultClass;
                    converted = true;

                    // This may not work, needs more testing. But it's good enough atm, it works for me (hopefully)
                    if (SerializationUtils.isSubObject(javaType)) {
                        objectValue.getSetter().accept(parent, value);
                        return;
                    }
                }
            }
            if (!converted) {
                throw new IllegalStateException("Converter not found. Converter required: " + conv);
            }
        }

        if (SerializationUtils.isValuePrimitiveOrString(javaType) && !converted) {
            objectValue.getSetter().accept(parent, value);
            return;
        }

        for (PropertyMapper<?> registeredPropertyMapper : PropertyMapperAnnotationHandler.REGISTERED_PROPERTY_MAPPERS) {
            if (registeredPropertyMapper.supports(javaType)) {
                Object result = registeredPropertyMapper.deMap(value, javaType, this, objectValue.getAnnotations(), objectValue);
                objectValue.getSetter().accept(parent, result);
                return;
            }
        }

        if (SerializationUtils.isSubObject(javaType)) {
            objectValue.getSetter().accept(parent, demapObject((MappedObject) value, javaType));
        }

    }

    public String writeValueAsString(Object object) {
        if (SerializationUtils.isValuePrimitiveOrString(object.getClass())) {
            return object.toString();
        }

        return serializationProvider.writeAsString(mapObject(object));
    }

    public void writeValueToFile(Object object, Path file) {
        serializationProvider.writeToFile(mapObject(object), file);
    }

    public <T> T readValueFromString(String string, Class<T> type) {
        if (SerializationUtils.isValuePrimitiveOrString(type)) {
            // fixme possibly wrong, because Integer.parse etc.
            return ReflectionUtils.castIfNecessary(type, string);
        }

        return demapObject(serializationProvider.readFromString(string, type), type);
    }

    public <T> T readValueFromFile(Path file, Class<T> type) {
        return demapObject(serializationProvider.readFromFile(file, type), type);
    }

    // Demap convert
                /*
            if (value.shouldConvert()) {
                Class<?> conv = ((Convert) value.getAnnotations().get(Convert.class)).converter();
                Class<?> javaType = value.getJavaType();
                boolean converted = false;

                for (ValueConverter<?, ?> converter : ConverterAnnotationHandler.REGISTERED_CONVERTERS) {
                    if (converter.supports(javaType) && conv.equals(converter.getClass())) {
                        Object result = converter.convert(value, false);

                        if (SerializationUtils.isValuePrimitiveOrString(result.getClass())) {
                           value.getSetter().accept(object, result);
                           break;
                        }

                        converted = true;
                    }
                }

                if (!converted) {
                    throw new IllegalStateException("Converter not found. Converter required: " + conv);
                }
            } */
}
