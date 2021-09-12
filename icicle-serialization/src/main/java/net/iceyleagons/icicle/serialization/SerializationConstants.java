package net.iceyleagons.icicle.serialization;

import net.iceyleagons.icicle.serialization.annotations.SerializeIgnore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static net.iceyleagons.icicle.utilities.StringUtils.containsIgnoresCase;

public final class SerializationConstants {

    public static <T> T generateInstance(Class<T> type) throws IllegalStateException {
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Object must contain one public empty constructor!", e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Could not instantiate object!");
        }
    }

    public static boolean isSubObject(Class<?> type) {
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

    public static boolean shouldIgnore(Field field) {
        return Modifier.isTransient(field.getModifiers()) && field.isAnnotationPresent(SerializeIgnore.class);
    }
}
