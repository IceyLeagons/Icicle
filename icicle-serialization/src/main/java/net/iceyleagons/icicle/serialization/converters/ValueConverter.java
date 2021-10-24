package net.iceyleagons.icicle.serialization.converters;

public interface ValueConverter<A, B> {

    A convertToSerializedField(B objectField);
    B convertToObjectField(A serializedField);

}
