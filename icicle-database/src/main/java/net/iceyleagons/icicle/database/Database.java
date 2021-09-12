package net.iceyleagons.icicle.database;

import net.iceyleagons.icicle.database.schema.CollectionSchema;

import java.util.Optional;
import java.util.Set;

public interface Database<K, V> {

    void save(V object);

    Optional<V> findById(K id);
    Set<V> findAll();

    void deleteById(K id);
    void deleteAll();

    void updateSchema(CollectionSchema updatedSchema);

    CollectionSchema getSchema();
}
