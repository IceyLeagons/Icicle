package net.iceyleagons.icicle.serialization;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.DefaultConverters;
import net.iceyleagons.icicle.serialization.converters.NoConvert;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.datastores.triple.UnmodifiableTriple;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@RequiredArgsConstructor
public class ObjectMapper {
    
    public <T> T unMapObject(ObjectDescriptor objectDescriptor, Class<T> wantedType) {
        if (!objectDescriptor.getObjectType().equals(wantedType)) return null;

        T parent = SerializationConstants.generateInstance(wantedType);

        for (Triple<String, Field, ObjectDescriptor> subObject : objectDescriptor.getSubObjects()) {
            Field field = subObject.getB();

            Object unMappedObject = unMapObject(subObject.getC(), field.getType());
            if (field.isAnnotationPresent(Convert.class)) {
                Class<?> converter = field.getAnnotation(Convert.class).converter();
                unMappedObject = convert(unMappedObject, converter, false);
            }

            ReflectionUtils.set(field, parent, unMappedObject);
        }

        //TODO collections not just arrays
        //TODO maps
        for (Triple<String, Field, List<ObjectDescriptor>> subObjectArray : objectDescriptor.getSubObjectArrays()) {
            Field field = subObjectArray.getB();
            List<ObjectDescriptor> content = subObjectArray.getC();

            Object[] array = GenericUtils.createGenericArray(field.getType(), content.size());

            for (int i = 0; i < content.size(); i++) {
                ObjectDescriptor desc = content.get(i);

                array[i] = unMapObject(desc, desc.getObjectType().getComponentType());
            }

            ReflectionUtils.set(field, parent, array);
        }

        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            Field field = valueField.getB();
            Object value = valueField.getC();

            if (field.isAnnotationPresent(Convert.class)) {
                Class<?> converter = field.getAnnotation(Convert.class).converter();
                value = convert(value, converter, false);
            } else if (DefaultConverters.converters.containsKey(field.getType()) && !field.isAnnotationPresent(NoConvert.class)) { //it's elseif, so custom @Converters can overwrite default ones
                value = convert(value, DefaultConverters.converters.get(field.getType()), false);
            }

            ReflectionUtils.set(field, parent, value);
        }

        return parent;
    }

    public ObjectDescriptor mapObject(Object object) {
        Class<?> type = object.getClass();

        Tuple<Field[], Field[]> fields = getFields(type);
        Field[] valueFields = fields.getA();
        Field[] subObjectFields = fields.getB();

        ObjectDescriptor objectDescriptor = new ObjectDescriptor(type);

        for (Field valueField : valueFields) {
            objectDescriptor.getValueFields().add(getFieldValues(valueField, object));
        }

        for (Field subObjectField : subObjectFields) {
            Triple<String, Field, Object> values = getFieldValues(subObjectField, object);
            Object value = values.getC();

            if (!subObjectField.isAnnotationPresent(NoConvert.class) && (
                    subObjectField.isAnnotationPresent(Convert.class) ||
                    DefaultConverters.converters.containsKey(subObjectField.getType()))) {
                // This is done like this to: a) prevent duplicate code, and most importantly b) so custom @Converters can overwrite default ones

                Object converted = subObjectField.isAnnotationPresent(Convert.class) ?
                        Objects.requireNonNull(convert(values.getC(), subObjectField.getAnnotation(Convert.class).converter(), true)) :
                        Objects.requireNonNull(convert(values.getC(), DefaultConverters.converters.get(subObjectField.getType()), true));

                if (SerializationConstants.isSubObject(converted.getClass())) {
                    ObjectDescriptor convertedValue = mapObject(converted);
                    objectDescriptor.getSubObjects().add(new UnmodifiableTriple<>(values.getA(), values.getB(), convertedValue));
                }

                objectDescriptor.getValueFields().add(new UnmodifiableTriple<>(values.getA(), values.getB(), converted));
                continue;
            }

            //TODO collections not just arrays
            //TODO maps
            if (subObjectField.getType().isArray()) {
                List<ObjectDescriptor> subArray = new ArrayList<>();

                for (int i = 0; i < Array.getLength(value); i++) {
                    ObjectDescriptor descriptor = mapObject(Array.get(value, i));
                    subArray.add(descriptor);
                }

                objectDescriptor.getSubObjectArrays().add(new UnmodifiableTriple<>(values.getA(), values.getB(), subArray));
                continue;
            }

            objectDescriptor.getSubObjects().add(new UnmodifiableTriple<>(values.getA(), values.getB(), mapObject(values.getC())));
        }

        return objectDescriptor;
    }

    private static Object convert(Object input, Class<?> converterClass, boolean serialize)  {
        try {
            Constructor<?> constructor = converterClass.getDeclaredConstructor();

            Object converterObject = constructor.newInstance();
            if (!(converterObject instanceof ValueConverter)) {
                throw new IllegalStateException("Converter must implement ValueConverter!");
            }

            ValueConverter<?,?> converter = getConverterFrom(converterClass);
            return convert(input, converter, serialize);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Converter must have 1 public empty constructor!", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static Object convert(Object input, ValueConverter<?,?> converter, boolean serialize)  {
        if (converter == null) throw new IllegalStateException("Converter is null!");

        return serialize ? converter.convertObjectToSerializedField(input) : converter.convertObjectToObjectField(input);
    }

    @SneakyThrows
    private static Triple<String, Field, Object> getFieldValues(Field field, Object parent) {
        String name = getFieldKey(field);
        return new UnmodifiableTriple<>(name, field, ReflectionUtils.get(field, parent, Object.class));
    }

    public static Triple<String, Field, List<ObjectDescriptor>> getFieldWithValue(Field field, List<ObjectDescriptor> value) {
        String name = getFieldKey(field);

        return new UnmodifiableTriple<>(name, field, value);
    }

    public static Triple<String, Field, ObjectDescriptor> getFieldWithValue(Field field, ObjectDescriptor value) {
        String name = getFieldKey(field);

        return new UnmodifiableTriple<>(name, field, value);
    }

    public static Triple<String, Field, Object> getFieldWithValue(Field field, Object value) {
        String name = getFieldKey(field);

        return new UnmodifiableTriple<>(name, field, value);
    }

    public static String getFieldKey(Field field) {
        return field.isAnnotationPresent(SerializedName.class) ? field.getAnnotation(SerializedName.class).value() : field.getName();
    }

    public static Tuple<Field[], Field[]> getFields(Class<?> clazz) {
        Set<Field> valueFields = new HashSet<>();
        Set<Field> subObjectFields = new HashSet<>();

        for (Field declaredField : clazz.getDeclaredFields()) {
            if (SerializationConstants.shouldIgnore(declaredField)) continue;

            if (SerializationConstants.isSubObject(declaredField.getType())) {
                subObjectFields.add(declaredField);
                continue;
            }

            valueFields.add(declaredField);
        }

        return new UnmodifiableTuple<>(valueFields.toArray(Field[]::new), subObjectFields.toArray(Field[]::new));
    }

    public static ValueConverter<?, ?> getConverterFrom(Class<?> clazz) {
        if (ValueConverter.class.isAssignableFrom(clazz)) {
            try {
                Object object = BeanUtils.getResolvableConstructor(clazz).newInstance();
                return (ValueConverter<?, ?>) object;
            } catch (Exception e) {
                throw new IllegalStateException("Could not create ValueConverter from class " + clazz.getName(), e);
            }
        }

        return null;
    }
}
