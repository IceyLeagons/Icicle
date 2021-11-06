package net.iceyleagons.icicle.serialization.map;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.serialization.converters.Convert;
import net.iceyleagons.icicle.serialization.converters.DefaultConverters;
import net.iceyleagons.icicle.serialization.converters.NoConvert;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.datastores.triple.UnmodifiableTriple;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.datastores.tuple.UnmodifiableTuple;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static net.iceyleagons.icicle.serialization.SerializationConstants.isSubObject;
import static net.iceyleagons.icicle.serialization.SerializationConstants.shouldIgnore;

public final class MapperUtils {

    static boolean isCollectionComponentSubObject(Field field) {
        Class<?> collectionComponentType = GenericUtils.getGenericTypeClass(field, 0);
        if (collectionComponentType == null)
            throw new IllegalStateException("Could not access type of Collection for field: " + field.getName());

        return isSubObject(collectionComponentType);
    }

    static Collection<Object> createCollectionFromField(Field field) {
        if (!Collection.class.isAssignableFrom(field.getType()))
            throw new IllegalStateException("Attempted to create Collection for a field whose type is non Collection assignable!");
        final Class<?> type = field.getType();

        try {
            Object obj;

            // These ifs are here as a default, if we want to be strict and not use these defaults when deserializing
            // use implementation rather than interfaces. Ex.: List --> ArrayList
            if (type.equals(List.class) || type.equals(Collection.class)) {
                obj = new ArrayList<>();
            } else if (type.equals(Set.class)) {
                obj = new HashSet<>();
            } else {
                obj = field.getType().getDeclaredConstructor().newInstance();
            }

            return (Collection<Object>) obj;
        } catch (Exception e) {
            throw new IllegalStateException("Could not create collection from field. ", e);
        }
    }

    static boolean isArrayType(Class<?> type) {
        return type.isArray() || Collection.class.isAssignableFrom(type);
    }

    static Object convert(Field field, Object input, boolean serialize) {
        return field.isAnnotationPresent(Convert.class) ?
                Objects.requireNonNull(convert(input, field.getAnnotation(Convert.class).converter(), serialize)) :
                Objects.requireNonNull(convert(input, DefaultConverters.converters.get(field.getType()), serialize));
    }

    static boolean shouldConvert(Field field) {
        return !field.isAnnotationPresent(NoConvert.class) &&
                (field.isAnnotationPresent(Convert.class) || DefaultConverters.converters.containsKey(field.getType()));
    }

    static Object convert(Object input, Class<?> converterClass, boolean serialize) {
        try {
            Constructor<?> constructor = converterClass.getDeclaredConstructor();

            Object converterObject = constructor.newInstance();
            if (!(converterObject instanceof ValueConverter)) {
                throw new IllegalStateException("Converter must implement ValueConverter!");
            }

            ValueConverter<?, ?> converter = getConverterFrom(converterClass);
            return convert(input, converter, serialize);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Converter must have 1 public empty constructor!", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    static Object convert(Object input, ValueConverter<?, ?> converter, boolean serialize) {
        if (converter == null) throw new IllegalStateException("Converter is null!");

        return serialize ? converter.convertObjectToSerializedField(input) : converter.convertObjectToObjectField(input);
    }

    @SneakyThrows
    static Triple<String, Field, Object> getFieldValues(Field field, Object parent) {
        String name = getFieldKey(field);
        Object val = ReflectionUtils.get(field, parent, Object.class);

        return new UnmodifiableTriple<>(name, field, val);
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
            if (shouldIgnore(declaredField)) continue;

            if (isSubObject(declaredField.getType())) {
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
