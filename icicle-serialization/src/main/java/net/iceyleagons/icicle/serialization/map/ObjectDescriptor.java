package net.iceyleagons.icicle.serialization.map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.iceyleagons.icicle.utilities.datastores.triple.Triple;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@EqualsAndHashCode
public class ObjectDescriptor {

    private final Class<?> objectType;
    private final Set<Triple<String, Field, Object>> valueFields = new HashSet<>();
    private final Set<Triple<String, Field, ObjectDescriptor>> subObjects = new HashSet<>();
    private final Set<Triple<String, Field, List<ObjectDescriptor>>> subObjectArrays = new HashSet<>();

    public ObjectDescriptor(Class<?> objectType) {
        this.objectType = objectType;
    }
}
