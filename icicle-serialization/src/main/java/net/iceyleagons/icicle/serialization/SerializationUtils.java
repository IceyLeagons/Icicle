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

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.iceyleagons.icicle.serialization.annotations.SerializedName;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static net.iceyleagons.icicle.utilities.StringUtils.containsIgnoresCase;

/**
 * @author TOTHTOMI
 * @version 1.2.0
 * @since Jun. 13, 2022
 */
public class SerializationUtils {

    public static Set<ObjectValue> getObjectValues(Class<?> javaType) {
        Set<ObjectValue> set = new ObjectArraySet<>(8);
        final Method[] methods = javaType.getDeclaredMethods();
        final Field[] fields = javaType.getDeclaredFields();

        for (Field declaredField : fields) {
            if (Modifier.isTransient(declaredField.getModifiers())) continue;


            FuzzyResolver.getGettersAndSetters(declaredField, methods).ifPresentOrElse(setterAndGetter -> {
                final Method getter = setterAndGetter.getA();
                final Method setter = setterAndGetter.getB();
                final String name = getter != null ? SerializationUtils.getCustomNameOrDefault(getter, declaredField.getName()) : SerializationUtils.getCustomNameOrDefault(declaredField, declaredField.getName());

                ObjectValue obj = new ObjectValue(declaredField.getType(), name, getter != null ? getAnnotations(getter) : getAnnotations(setter), // Either getter or setter must be present. Otherwise the optional is empty
                        (parent, value) -> {
                            if (setter != null) {
                                ReflectionUtils.invoke(setter, parent, Void.class, value);
                            } else {
                                ReflectionUtils.set(declaredField, parent, value);
                            }
                        },
                        parent -> {
                            if (getter != null) {
                                return ReflectionUtils.invoke(getter, parent, Object.class);
                            } else {
                                return ReflectionUtils.get(declaredField, parent, Object.class);
                            }
                        },
                        index -> GenericUtils.getGenericTypeClass(declaredField, index));

                set.add(obj);
            }, () -> {
                final String name = SerializationUtils.getCustomNameOrDefault(declaredField, declaredField.getName());

                ObjectValue obj = new ObjectValue(declaredField.getType(), name, getAnnotations(declaredField),
                        (parent, value) -> ReflectionUtils.set(declaredField, parent, value),
                        parent -> ReflectionUtils.get(declaredField, parent, Object.class),
                        index -> GenericUtils.getGenericTypeClass(declaredField, index));

                set.add(obj);
            });
        }

        for (Tuple<Method, Method> standaloneGettersAndSetters : FuzzyResolver.getStandaloneGettersAndSetters(methods, fields)) {
            // Inside these every method must be not null, otherwise FuzzyResolver skips them.
            Method getter = standaloneGettersAndSetters.getA();
            Method setter = standaloneGettersAndSetters.getB();

            ObjectValue obj = new ObjectValue(
                    getter.getReturnType(),
                    SerializationUtils.getCustomNameOrDefault(getter, FuzzyResolver.getPropertyNameFromMethod(getter).orElseThrow(() -> new IllegalStateException("Could not get name for getter."))),
                    getAnnotations(getter),
                    (parent, value) -> ReflectionUtils.invoke(setter, parent, Void.class, value),
                    parent -> ReflectionUtils.invoke(getter, parent, Object.class),
                    index -> GenericUtils.getGenericTypeClass(getter, index));

            set.add(obj);
        }

        return set;
    }

    public static Map<Class<? extends Annotation>, Annotation> getAnnotations(AccessibleObject object) {
        object.setAccessible(true);
        return Arrays.stream(object.getAnnotations()).collect(Collectors.toMap(Annotation::annotationType, a -> a));
    }

    public static String getCustomNameOrDefault(AccessibleObject obj, String defaultValue) {
        return obj.isAnnotationPresent(SerializedName.class) ? obj.getAnnotation(SerializedName.class).value() : defaultValue;
    }

    public static boolean isValuePrimitiveOrString(Class<?> type) {
        return type.isPrimitive() || type.equals(String.class);
    }

    public static boolean isEnum(Class<?> type) {
        return type.isEnum();
    }

    public static boolean isArray(Class<?> type) {
        return type.isArray();
    }

    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type);
    }

    public static boolean isMap(Class<?> type) {
        return Map.class.isAssignableFrom(type);
    }

    public static Object createMapFromType(Class<?> t) {
        if (t.equals(Map.class)) {
            return new HashMap<>();
        } else if (t.equals(ConcurrentHashMap.class)) {
            return new ConcurrentHashMap<>();
        } else {
            throw new IllegalStateException("Unsupported type for collection (deserialization): " + t.getName());
        }
    }

    public static <T> T getValueAs(Class<T> clazz, Object object) {
        return clazz.isInstance(object) ? clazz.cast(object) : null;
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


    public static boolean isSubObject(Class<?> type) {
        //if (type.equals(SerializedObject.class)) return true;

        // We check for types like this due to arrays. We could check with conventional stuff (#isArray(), etc.), but because primitives and objects can also be used
        // (int[], Integer[]), we rather do it this way to save space in code, and make the code more readable.
        String typeName = type.getTypeName();
        return !containsIgnoresCase(typeName, "string") &&
                !containsIgnoresCase(typeName, "int") &&
                !containsIgnoresCase(typeName, "boolean") &&
                !containsIgnoresCase(typeName, "long") &&
                !containsIgnoresCase(typeName, "float") &&
                !containsIgnoresCase(typeName, "double") &&
                !containsIgnoresCase(typeName, "short") &&
                !containsIgnoresCase(typeName, "byte") &&
                !containsIgnoresCase(typeName, "char");
    }

}
