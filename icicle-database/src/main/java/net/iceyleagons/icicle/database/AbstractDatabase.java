package net.iceyleagons.icicle.database;

import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.map.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractDatabase<K, V> implements Database<K, V> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Class<K> keyClass = (Class<K>) GenericUtils.getGenericTypeClass(this.getClass(), 0);
    private final Class<V> valueClass = (Class<V>) GenericUtils.getGenericTypeClass(this.getClass(), 1);

    protected abstract void save(K id, ObjectDescriptor objectDescriptor);

    protected abstract ObjectDescriptor getById(K id);
    protected abstract Set<ObjectDescriptor> getAll();

    @Override
    public void save(V object) {
        ObjectDescriptor objectDescriptor = objectMapper.mapObject(object);
        K id = null; // TODO: find out id from fields etc.

        save(id, objectDescriptor);
    }

    @Override
    public Optional<V> findById(K id) {
        return getFrom(getById(id));
    }

    @Override
    public Set<V> findAll() {
        Set<V> resultSet = new HashSet<>();

        for (ObjectDescriptor objectDescriptor : getAll()) {
            getFrom(objectDescriptor).ifPresent(resultSet::add);
        }

        return resultSet;
    }

    private Optional<V> getFrom(ObjectDescriptor objectDescriptor) {
        return objectDescriptor == null ? Optional.empty() : Optional.ofNullable(objectMapper.unMapObject(objectDescriptor, valueClass));
    }
}
