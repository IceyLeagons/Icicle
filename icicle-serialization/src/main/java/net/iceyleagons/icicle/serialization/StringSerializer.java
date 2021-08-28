package net.iceyleagons.icicle.serialization;

public interface StringSerializer {

    String serializeToString(Object object);
    Object deserializeFromString(Object object);

}
