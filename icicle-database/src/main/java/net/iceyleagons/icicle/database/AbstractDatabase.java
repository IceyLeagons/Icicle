package net.iceyleagons.icicle.database;

import net.iceyleagons.icicle.database.schema.CollectionSchema;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public abstract class AbstractDatabase<K, V> implements Database<K, V> {

    private Class<V> typeOfV;
    private CollectionSchema schema = null;

    @SuppressWarnings("unchecked")
    public AbstractDatabase() {
        //Hopefully this works...
        this.typeOfV = (Class<V>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    }

    protected abstract void save(K id, Map<String, Object> values);

    protected abstract Map<String, Object> getById(K id);
    protected abstract Set<Map<String, Object>> getAll();

    protected abstract void onSchemaUpdate(CollectionSchema schema);

    @Override
    public void save(V object) {
       // Map<String, Tuple<Field, Object>> mappedObject = ObjectMapper.mapObjectWithFields(object);

        if (schema == null) {
            //TODO generate schema
        }

       // save(null, ObjectMapper.convertToObjectValueMap(mappedObject));
    }

    @Override
    public Optional<V> findById(K id) {
        return Optional.ofNullable(getFromValues(getById(id)));
    }

    @Override
    public Set<V> findAll() {
        Set<V> set = new HashSet<>();

        for (Map<String, Object> stringObjectMap : getAll()) {
            V value = getFromValues(stringObjectMap);
            if (value == null) continue;

            set.add(value);
        }

        return set;
    }

    @Override
    public void updateSchema(CollectionSchema updatedSchema) {
        this.schema = updatedSchema;

        onSchemaUpdate(this.schema);
    }

    @Override
    public CollectionSchema getSchema() {
        return this.schema;
    }

    @Nullable
    private V getFromValues(Map<String, Object> values) {
        if (values == null) return null;

        return null;//ObjectMapper.toObject(values, typeOfV);
    }
}
