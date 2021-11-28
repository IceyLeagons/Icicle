package net.iceyleagons.icicle.serialization.converters;

/**
 * @param <A> the object type
 * @param <B> the serialized type
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
public interface ValueConverter<A, B> {

    A fromSerialized(B serialized) throws Exception;

    B serialize(A toSerialize) throws Exception;

    Class<A> getAClass();

    Class<B> getBClass();

}
