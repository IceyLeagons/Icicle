package net.iceyleagons.icicle.serialization.converters.builtin;

import net.iceyleagons.icicle.serialization.converters.Converter;
import net.iceyleagons.icicle.serialization.converters.ValueConverter;
import org.json.JSONObject;

@Converter
public class JSONToStringConverter implements ValueConverter<String, JSONObject> {

    @Override
    public String convertToSerializedField(JSONObject objectField) {
        return objectField.toString();
    }

    @Override
    public JSONObject convertToObjectField(String serializedField) {
        return new JSONObject(serializedField);
    }
}
