package net.iceyleagons.icicle.serialization.serializers.impl;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.serializers.ComplexSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.json.JSONObject;

import java.util.Map;

@Getter
@Setter
public class JsonSerializer implements ComplexSerializer<JSONObject> {

    private boolean prettyPrint;

    public JsonSerializer(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public JsonSerializer() {
        this(true);
    }

    @Override
    public JSONObject serializeObject(Object object) {
        JSONObject empty = new JSONObject();
        Map<String, Object> values = ObjectMapper.mapObject(object);

        values.forEach(empty::put);
        return empty;
    }

    @Override
    public String serializeToString(Object object) {
        return serializeObject(object).toString(prettyPrint ? 2 : 0);
    }

    @Override
    public void writeToFile(Object object, AdvancedFile file) {
        file.setContent(serializeToString(object));
    }

    @Override
    public <T> T deserializeObject(Class<T> wantedType, JSONObject value) {
        return ObjectMapper.generateObjectFromData(wantedType, value.toMap());
    }

    @Override
    public <T> T deserializeFromString(Class<T> wantedType, String value) {
        return deserializeObject(wantedType, new JSONObject(value));
    }

    @Override
    public <T> T readFromFile(Class<T> wantedType, AdvancedFile file) {
        return deserializeFromString(wantedType, file.getContent(false));
    }
}
