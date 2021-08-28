package net.iceyleagons.icicle.serialization;

public interface ObjectSerializer<T> {

    T serializeObject(Object object);
    Object deserializeObject(T value);

}
