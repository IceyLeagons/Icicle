/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
