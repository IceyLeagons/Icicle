package net.iceyleagons.icicle.serialization.serializers;

/**
 * A serializer interface that combines all the other serializers for less code.
 */
public interface ComplexSerializer<T> extends FileSerializer<T>, StringSerializer<T> { }
