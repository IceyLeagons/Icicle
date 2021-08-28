package net.iceyleagons.icicle.serialization.json;

import lombok.Getter;
import lombok.Setter;
import net.iceyleagons.icicle.serialization.FileSerializer;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.ObjectSerializer;
import net.iceyleagons.icicle.serialization.StringSerializer;
import net.iceyleagons.icicle.utilities.file.AdvancedFile;
import org.json.JSONObject;

import java.util.Map;

@Getter
@Setter
public class JsonSerializer implements ObjectSerializer<JSONObject>, FileSerializer, StringSerializer {

    private boolean prettyPrint;

    public JsonSerializer(boolean prettyPrint) {
        this.prettyPrint = prettyPrint;
    }

    public JsonSerializer() {
        this(true);
    }

    @Override
    public void writeToFile(AdvancedFile file) {

    }

    @Override
    public void readFromFile(AdvancedFile file) {

    }

    @Override
    public JSONObject serializeObject(Object object) {
        JSONObject empty = new JSONObject();
        Map<String, Object> values = ObjectMapper.mapObject(object);

        values.forEach((name, value) -> {
            empty.put(name, value);
        });

        return empty;
    }

    @Override
    public Object deserializeObject(JSONObject value) {
        return null;
    }

    @Override
    public String serializeToString(Object object) {
        return serializeObject(object).toString(prettyPrint ? 2 : 0);
    }

    @Override
    public Object deserializeFromString(Object object) {
        return null;
    }
}
