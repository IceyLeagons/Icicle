package net.iceyleagons.icicle.serialization.map;

import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.utilities.ReflectionUtils;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;
import net.iceyleagons.icicle.utilities.datastores.triple.UnmodifiableTriple;
import net.iceyleagons.icicle.utilities.datastores.tuple.Tuple;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.iceyleagons.icicle.serialization.SerializationConstants.generateInstance;
import static net.iceyleagons.icicle.serialization.SerializationConstants.isSubObject;
import static net.iceyleagons.icicle.serialization.map.MapperUtils.*;

@RequiredArgsConstructor
public class ObjectMapper {

    // TOTHTOMI: This class took me ages to make, and it makes my head hurt. You could try to understand it,
    //           although I suggest not to. Imagine if I need to debug this sh*t.
    //           PS.: It may look complex, but it's actually not that bad. And it serialized my 84ms avg.


    // TODO deserialization of collections does not work --> collections are not populated with values
    // TODO make sure ser./deser. of collections happen after Converters, and that they can be subject to convertion
    // TODO move complex loops or parts of both ser.&deser. to separate methods

    // TODO ser./deser. of maps
    // TODO comment things --> more understandable code


    public <T> T unMapObject(ObjectDescriptor objectDescriptor, Class<T> wantedType) {
        if (!objectDescriptor.getObjectType().equals(wantedType)) return null;

        final T parent = generateInstance(wantedType);

        for (Triple<String, Field, ObjectDescriptor> subObject : objectDescriptor.getSubObjects()) {
            final Field field = subObject.getB();

            Object unMappedObject = unMapObject(subObject.getC(), field.getType());
            if (shouldConvert(field)) {
                unMappedObject = convert(field, unMappedObject, false);
            }

            ReflectionUtils.set(field, parent, unMappedObject);
        }

        handleArrayTypeUnMapping(parent, objectDescriptor);

        for (Triple<String, Field, Object> valueField : objectDescriptor.getValueFields()) {
            final Field field = valueField.getB();
            Object value = valueField.getC();

            if (shouldConvert(field))
                value = convert(field, value, false);

            ReflectionUtils.set(field, parent, value);
        }

        return parent;
    }

    public ObjectDescriptor mapObject(Object object) {
        final Class<?> type = object.getClass();

        final Tuple<Field[], Field[]> fields = getFields(type);
        final Field[] valueFields = fields.getA();
        final Field[] subObjectFields = fields.getB();

        final ObjectDescriptor objectDescriptor = new ObjectDescriptor(type);

        for (Field valueField : valueFields) {
            objectDescriptor.getValueFields().add(getFieldValues(valueField, object));
        }

        handleSubObjectFieldsMapping(subObjectFields, object, objectDescriptor);

        return objectDescriptor;
    }

    private void handleSubObjectFieldsMapping(Field[] subObjectFields, Object parent, ObjectDescriptor objectDescriptor) {
        for (Field subObjectField : subObjectFields) {
            final Triple<String, Field, Object> values = getFieldValues(subObjectField, parent);
            final Object value = values.getC();

            // This is done like this to: a) prevent duplicate code, and most importantly b) so custom @Converters can overwrite default ones
            if (shouldConvert(subObjectField)) {
                final Object converted = convert(subObjectField, values.getC(), true);

                if (isSubObject(converted.getClass())) {
                    ObjectDescriptor convertedValue = mapObject(converted);
                    objectDescriptor.getSubObjects().add(new UnmodifiableTriple<>(values.getA(), values.getB(), convertedValue));
                }

                objectDescriptor.getValueFields().add(new UnmodifiableTriple<>(values.getA(), values.getB(), converted));
                continue;
            }

            if (isArrayType(subObjectField.getType())) {
                handleArrayTypeMapping(objectDescriptor, values.getA(), values.getB(), value);
                continue;
            }

            //TODO maps
            objectDescriptor.getSubObjects().add(new UnmodifiableTriple<>(values.getA(), values.getB(), mapObject(values.getC())));
        }
    }

    private void handleArrayTypeMapping(ObjectDescriptor objectDescriptor, String name, Field field, Object value) {
        if (isArrayType(field.getType())) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                final Collection<?> collection = (Collection<?>) value;

                if (isCollectionComponentSubObject(field)) {
                    final List<ObjectDescriptor> subArray = new ArrayList<>();

                    for (Object o : collection) {
                        ObjectDescriptor descriptor = mapObject(o);
                        subArray.add(descriptor);
                    }

                    objectDescriptor.getSubObjectArrays().add(new UnmodifiableTriple<>(name, field, subArray));
                    return;
                }

                objectDescriptor.getValueFields().add(new UnmodifiableTriple<>(name, field, collection.toArray()));
                return;
            }


            final List<ObjectDescriptor> subArray = new ArrayList<>();
            for (int i = 0; i < Array.getLength(value); i++) {
                ObjectDescriptor descriptor = mapObject(Array.get(value, i));
                subArray.add(descriptor);
            }

            objectDescriptor.getSubObjectArrays().add(new UnmodifiableTriple<>(name, field, subArray));
        }
    }

    private void handleArrayTypeUnMapping(Object parent, ObjectDescriptor objectDescriptor) {
        for (Triple<String, Field, List<ObjectDescriptor>> subObjectArray : objectDescriptor.getSubObjectArrays()) {
            final Field field = subObjectArray.getB();
            final List<ObjectDescriptor> content = subObjectArray.getC();

            if (Collection.class.isAssignableFrom(field.getType())) {
                //Dealing with collection
                final Collection<Object> collection = createCollectionFromField(field);

                for (ObjectDescriptor descriptor : content) {
                    Object toAdd = unMapObject(descriptor, descriptor.getObjectType().getComponentType());
                    collection.add(toAdd);
                }

                ReflectionUtils.set(field, parent, collection);
            } else if (field.getType().isArray()) {
                //Dealing with array
                final Object[] array = GenericUtils.createGenericArray(field.getType(), content.size());

                for (int i = 0; i < content.size(); i++) {
                    ObjectDescriptor desc = content.get(i);

                    array[i] = unMapObject(desc, desc.getObjectType().getComponentType());
                }

                ReflectionUtils.set(field, parent, array);
            }
        }
    }
}
