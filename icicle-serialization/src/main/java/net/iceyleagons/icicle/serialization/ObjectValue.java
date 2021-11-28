package net.iceyleagons.icicle.serialization;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;

import static net.iceyleagons.icicle.utilities.StringUtils.containsIgnoresCase;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
@Getter
@EqualsAndHashCode
public class ObjectValue {

    private final Class<?> javaType;
    private final Field field;
    private final String key; //name
    private final Object value;

    public ObjectValue(Class<?> javaType, Field field, Object value) {
        this.javaType = javaType;
        this.field = field;
        this.key = ObjectMapper.getName(field);
        this.value = value;
    }

    public static boolean isValuePrimitiveOrString(Class<?> type) {
        return type.isPrimitive() || type.equals(String.class);
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

    public static boolean isSubObject(Class<?> type) {
        if (type.equals(MappedObject.class)) return true;

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

    public boolean isValuePrimitiveOrString() {
        return isValuePrimitiveOrString(this.javaType);
    }

    public boolean isArray() {
        return isArray(this.javaType);
    }

    public boolean isCollection() {
        return isCollection(this.javaType);
    }

    public boolean isMap() {
        return isMap(this.javaType);
    }

    public boolean isSubObject() {
        return isSubObject(this.javaType);
    }
}
