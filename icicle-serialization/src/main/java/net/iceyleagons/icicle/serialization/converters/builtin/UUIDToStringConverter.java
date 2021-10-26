package net.iceyleagons.icicle.serialization.converters.builtin;

import net.iceyleagons.icicle.serialization.converters.Converter;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;

import java.util.UUID;

@Converter
public class UUIDToStringConverter implements ValueConverter<String, UUID> {

    @Override
    public String convertToSerializedField(UUID objectField) {
        return objectField.toString();
    }

    @Override
    public UUID convertToObjectField(String serializedField) {
        return UUID.fromString(serializedField);
    }
}
