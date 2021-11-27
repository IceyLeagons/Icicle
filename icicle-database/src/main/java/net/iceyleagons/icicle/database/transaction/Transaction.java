package net.iceyleagons.icicle.database.transaction;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 26, 2021
 */
public interface Transaction<T, I> {

    Transaction<T, I> retrieveAll();

    Transaction<T, I> retrieve(I id);

    Transaction<T, I> save(T object);

    Transaction<T, I> saveAll(T... objects);

    Transaction<T, I> delete(T object);
    Transaction<T, I> deleteWithId(I id);

    Outcome<T, I> commit();
    Outcome<T, I> rollback();
}
