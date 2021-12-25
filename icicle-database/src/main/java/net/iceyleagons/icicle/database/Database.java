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

package net.iceyleagons.icicle.database;

import net.iceyleagons.icicle.database.transaction.Outcome;
import net.iceyleagons.icicle.database.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 26, 2021
 */
public interface Database<T, I> {

    DatabaseDriver driver = null;

    // Fast access --> not transactional, in a sense that rollback is not supported
    default List<T> retrieveAll() {
        Outcome<T, I> t = startTransaction().retrieveAll().commit();
        return new ArrayList<>(t.getRetrieved().values());
    }

    default T retrieve(I id) {
        Outcome<T, I> t = startTransaction().retrieve(id).commit();
        return t.getRetrieved().get(id);
    }

    /**
     * @param object
     * @return the same object with the ID field filled out
     */
    default T save(T object) {
        Outcome<T, I> t = startTransaction().save(object).commit();
        return t.getRetrieved().values().stream().findFirst().orElse(null);
    }

    /**
     * @param objects
     * @return the same objects with the ID field filled out
     */
    default List<T> saveAll(T... objects) {
        Outcome<T, I> t = startTransaction().saveAll(objects).commit();
        return t.getRetrieved().values().stream().toList();
    }

    default void delete(T object) {
        startTransaction().delete(object).commit();
    }

    default void deleteWithId(I id) {
        startTransaction().deleteWithId(id).commit();
    }

    Transaction<T, I> startTransaction();

    String getName();

    void changeDriver(DatabaseDriver driver);

    DatabaseDriver getDriver();
}
