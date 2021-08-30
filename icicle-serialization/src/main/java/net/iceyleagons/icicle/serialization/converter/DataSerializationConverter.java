package net.iceyleagons.icicle.serialization.converter;

//A - object data
//B - serialized data
public interface DataSerializationConverter<A, B> {

    A convertToEntityAttribute(B serializedData);
    B convertToStorageAttribute(A objectData);

    default Object convertToStorageAttributeFromObject(Object object) {
        return this.convertToStorageAttribute((A) object);
    }

    default A convertToEntityAttributeFromObject(Object object) {
        return this.convertToEntityAttribute((B) object);
    }
}
