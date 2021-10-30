package net.iceyleagons.icicle.database.types;

import net.iceyleagons.icicle.database.AbstractDatabase;
import net.iceyleagons.icicle.database.DatabaseUtils;
import net.iceyleagons.icicle.database.sql.SQLTableSchema;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;

import java.util.Optional;
import java.util.Set;

public class SQLDatabase<K, V> extends AbstractDatabase<K, V> {

    @Override
    protected void save(ObjectDescriptor objectDescriptor, K id) {
        final Class<?> objectType = objectDescriptor.getObjectType();
        final String datastoreName = DatabaseUtils.getDatastoreName(objectType);

        if (!tableExists(datastoreName)) {
            String query = SQLTableSchema.of(objectDescriptor).toSQLTableCreate(datastoreName);
        }
    }

    @Override
    protected Optional<ObjectDescriptor> findById(K id, Class<V> type) {
        return Optional.empty();
    }

    @Override
    protected Set<ObjectDescriptor> findAll(Class<V> type) {
        return null;
    }

    @Override
    protected K generateNewId() {
        return null;
    }

    @Override
    public void deleteById(K id) {

    }

    @Override
    public void deleteAll() {

    }

    private boolean tableExists(String name) {
        return false;
    }
}
