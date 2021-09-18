package net.iceyleagons.icicle.utilities.generic;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods to make dealing with generic types easier!
 *
 * @version 1.0.0
 * @since Aug. 28, 2021
 * @author TOTHTOMI
 */
public final class GenericUtils {

    @SuppressWarnings("unchecked")
    public static <T> T[] createGenericArray(Class<T> type, int size) {
        return (T[]) Array.newInstance(type, size);
    }

    public static Class<?> getTypeVariableType(Class<?> subClass, TypeVariable<?> typeVariable) {
        Map<TypeVariable<?>, Type> subMap = new HashMap<>();
        Class<?> superClass;

        while ((superClass = subClass.getSuperclass()) != null) {

            Map<TypeVariable<?>, Type> superMap = new HashMap<>();
            Type superGeneric = subClass.getGenericSuperclass();
            if (superGeneric instanceof ParameterizedType) {

                TypeVariable<?>[] typeParams = superClass.getTypeParameters();
                Type[] actualTypeArgs = ((ParameterizedType) superGeneric).getActualTypeArguments();

                for (int i = 0; i < typeParams.length; i++) {
                    Type actualType = actualTypeArgs[i];
                    if (actualType instanceof TypeVariable) {
                        actualType = subMap.get(actualType);
                    }
                    if (typeVariable == typeParams[i]) return (Class<?>) actualType;
                    superMap.put(typeParams[i], actualType);
                }
            }
            subClass = superClass;
            subMap = superMap;
        }
        return null;
    }

    public static Class<?> getTypeParameterType(Class<?> subClass, Class<?> superClass, int typeParameterIndex) {
        return getTypeVariableType(subClass, superClass.getTypeParameters()[typeParameterIndex]);
    }

    public static Class<?> getFieldType(Class<?> clazz, AccessibleObject element) {
        Class<?> type = null;
        Type genericType = null;

        if (element instanceof Field) {
            type = ((Field) element).getType();
            genericType = ((Field) element).getGenericType();
        } else if (element instanceof Method) {
            type = ((Method) element).getReturnType();
            genericType = ((Method) element).getGenericReturnType();
        }

        if (genericType instanceof TypeVariable) {
            Class<?> typeVariableType = getTypeVariableType(clazz, (TypeVariable) genericType);
            if (typeVariableType != null) {
                type = typeVariableType;
            }
        }

        return type;
    }

}
