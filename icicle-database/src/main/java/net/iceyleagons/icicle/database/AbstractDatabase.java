package net.iceyleagons.icicle.database;

import lombok.Getter;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.ObjectDescriptor;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.generic.acessors.TwoTypeAccessor;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public abstract class AbstractDatabase<K, V> extends TwoTypeAccessor<K, V> implements Database<K, V> {

    private final Class<K> kClass;
    private final Class<V> vClass;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public AbstractDatabase() {
        this.kClass = super.getATypeClass();
        this.vClass = super.getBTypeClass();
    }

    protected abstract void save(ObjectDescriptor objectDescriptor, K id);
    protected abstract Optional<ObjectDescriptor> findById(K id, Class<V> type);
    protected abstract Set<ObjectDescriptor> findAll(Class<V> type);
    protected abstract K generateNewId();

    @Override
    public void save(V object) {
        final Field idField = DatabaseUtils.getIdField(object);
        Asserts.notNull(idField, "ID is not found for DB entity.");

        K id = ReflectionUtils.get(idField, object, this.kClass);
        if (DatabaseUtils.shouldAutoGenerate(idField) && id == null) id = generateNewId();

        final ObjectDescriptor objectDescriptor = this.objectMapper.mapObject(object);
        this.save(objectDescriptor, id);
    }

    @Override
    public Optional<V> findById(K id) {
        final Optional<ObjectDescriptor> dbResult = this.findById(id, this.vClass);
        return dbResult.isEmpty() ?  Optional.empty() : Optional.ofNullable(new ObjectMapper().unMapObject(dbResult.get(), this.vClass));
    }

    @Override
    public Set<V> findAll() {
        final Set<ObjectDescriptor> objectDescriptorSet = this.findAll(this.vClass);
        return objectDescriptorSet.stream().map(o -> this.objectMapper.unMapObject(o, this.vClass)).collect(Collectors.toSet());
    }
}
