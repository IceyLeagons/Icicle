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
