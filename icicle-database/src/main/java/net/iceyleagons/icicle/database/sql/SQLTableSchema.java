package net.iceyleagons.icicle.database.sql;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SQLTableSchema {

    private final Map<String, String> schema;

    public String toSQLTableCreate(String tableName) {
        StringBuilder stringBuilder = new StringBuilder("CREATE TABLE ");
        stringBuilder.append(tableName).append(" (");

        for (Map.Entry<String, String> stringStringEntry : schema.entrySet()) {
            stringBuilder.append(stringStringEntry.getKey()).append(" ").append(stringStringEntry.getValue()).append(", ");
        }

        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), ""); //removing the last ", "
        stringBuilder.append(");");

        return stringBuilder.toString();
    }

    public static SQLTableSchema of(ObjectDescriptor objectDescriptor) {
        final Map<String, String> schema = new HashMap<>();

        // TODO lists, sub-objects etc.

        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            String name = valueField.getA();

            Class<?> type = valueField.getB().getType();
            String sqlType = SQLUtils.getSQLTypeFromJavaType(type);

            schema.put(name, sqlType);
        }

        return new SQLTableSchema(schema);
    }
}
