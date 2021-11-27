package net.iceyleagons.icicle.serialization.serializers;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public interface StringSerializer {

    String serializeToString(Object object);
    <T> T deSerializeFromString(String input, Class<T> wantedType);

}
