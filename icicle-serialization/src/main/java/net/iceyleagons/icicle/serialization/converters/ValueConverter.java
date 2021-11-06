package net.iceyleagons.icicle.serialization.converters;

/**
 * @param <A> the serialized type
 * @param <B> the object type
 */
public interface ValueConverter<A, B> {

    A convertToSerializedField(B objectField);

    B convertToObjectField(A serializedField);

    default Object convertObjectToSerializedField(Object input) {
        return convertToSerializedField(castToB(input));
    }

    default Object convertObjectToObjectField(Object input) {
        return convertToObjectField(castToA(input));
    }

    private A castToA(Object object) {
        return (A) object;
    }

    private B castToB(Object object) {
        return (B) object;
    }
}
