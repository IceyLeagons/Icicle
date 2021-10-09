package net.iceyleagons.icicle.database.sql;

public final class SQLUtils {

    public static String getSQLTypeFromJavaType(Class<?> type) {
        if (type.equals(boolean.class)) {
            return "BIT";
        } else if (type.equals(byte.class) || type.equals(short.class)) {
            return "TINYINT";
        } else if (type.equals(int.class)) {
            return "INTEGER";
        } else if (type.equals(long.class)) {
            return "BIGINT";
        } else if (type.equals(float.class)) {
            return "REAL"; // (or REAL)
        } else if (type.equals(double.class)) {
            return "DOUBLE";
        } else if (type.equals(byte[].class)) {
            return "LONGVARBINARY";
        } else if (type.equals(String.class)) {
            return "LONGTEXT"; //should be varchar, but with LONGTEXT we don't have to worry about sizes
        }

        return null;
    }

    public static String appendSize(String sqlType, int size) {
        return sqlType + "(" + size + ")";
    }
}
