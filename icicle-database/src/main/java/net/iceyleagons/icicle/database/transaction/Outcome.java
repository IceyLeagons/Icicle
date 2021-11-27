package net.iceyleagons.icicle.database.transaction;

import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 26, 2021
 */
@Getter
public class Outcome<T, I> {

    public static final int STATUS_OK = 0;
    public static final int STATUS_CONFLICT = 1; // Just preserved atm
    public static final int STATUS_QUERY_ERROR = 2; //problems with the query (ex.: malformed query)
    public static final int STATUS_DB_ERROR = 3; //database problems (ex.: could not save an entry in the session)
    public static final int STATUS_CONNECTION_ERROR = 4; //database connection errors (lost connection, etc.)

    private final Transaction<T, I> session = null;
    private int statusCode;
    private int affectedEntries;
    private Map<I, T> retrieved = new ConcurrentHashMap<>();
    private Map<T, I> saved = new ConcurrentHashMap<>(); //used for retrieving IDs

    public I getIdForSavedObject(T object) {
        return saved.get(object);
    }
}
