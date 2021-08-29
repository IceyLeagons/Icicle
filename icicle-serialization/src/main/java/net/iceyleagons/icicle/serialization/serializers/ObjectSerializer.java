package net.iceyleagons.icicle.serialization.serializers;

public interface ObjectSerializer<V> {

    V serializeObject(Object object);
    <T> T deserializeObject(Class<T> wantedType, V value);

}
