package net.iceyleagons.icicle.serialization.serializers;

public interface StringSerializer<V> extends ObjectSerializer<V> {

    String serializeToString(Object object);
    <T> T deserializeFromString(Class<T> wantedType, String value);

}
