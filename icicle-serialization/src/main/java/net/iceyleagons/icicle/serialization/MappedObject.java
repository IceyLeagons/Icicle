package net.iceyleagons.icicle.serialization;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class MappedObject {

    private final Class<?> javaType;
    private final Set<ObjectValue> values = new HashSet<>();

    public void addValue(ObjectValue value) {
        values.add(value);
    }
}
