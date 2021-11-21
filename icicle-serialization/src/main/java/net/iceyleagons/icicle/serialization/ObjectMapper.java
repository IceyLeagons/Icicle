package net.iceyleagons.icicle.serialization;

import lombok.SneakyThrows;
import net.iceyleagons.icicle.core.utils.BeanUtils;
import net.iceyleagons.icicle.serialization.annotations.SerializeIgnore;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.utilities.ReflectionUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;


/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public class ObjectMapper {

    @SneakyThrows
    protected <T> T demapObject(MappedObject mappedObject, Class<T> wantedType) {
        final Class<?> type = mappedObject.getJavaType();
        if (!wantedType.isAssignableFrom(type)) throw new IllegalArgumentException("Wanted type is not assignable from the mapped object!");



        Constructor<?> constructor = BeanUtils.getResolvableConstructor(type);;
        if (constructor.getParameterTypes().length != 0) throw new IllegalStateException("Object must have an empty constructor!");

        Object object = constructor.newInstance();

        for (ObjectValue value : mappedObject.getValues()) {
            Field field = value.getField();

            if (value.isValuePrimitiveOrString() || (value.isArray() && !value.isCollection())) {
                ReflectionUtils.set(field, object, value.getValue());
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
            final Class<?> fieldType = declaredField.getType();
            ObjectValue result;

            if (ObjectValue.isValuePrimitiveOrString(fieldType) || (ObjectValue.isArray(fieldType) && !ObjectValue.isCollection(fieldType))) {
                result = new ObjectValue(fieldType, declaredField, ReflectionUtils.get(declaredField, object, Object.class));
            } else if (ObjectValue.isCollection(fieldType)) {
                Object[] collection = Objects.requireNonNull(ReflectionUtils.get(declaredField, object, Collection.class)).toArray();
                Object[] collArray = new Object[collection.length];

                for (int i = 0; i < collection.length; i++) {
                    Object colValue = collection[i];
                    collArray[i] = mapObject(colValue);
                }

                result = new ObjectValue(fieldType, declaredField, collArray);
            } else if (ObjectValue.isMap(fieldType)) {
                throw new IllegalStateException("Unsupported, but planned value type: " + fieldType.getName());
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

    public static boolean isSupportedCollection(Class<?> t) {
        return t.equals(List.class) || t.equals(Collection.class) || t.equals(ArrayList.class) || t.equals(Set.class) || t.equals(HashSet.class);
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
}
