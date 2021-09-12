package net.iceyleagons.icicle.database.schema;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.Field;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class CollectionEntry {

    private final String name;
    private final boolean id;
    private final Class<?> type;
    private final Field correspondingField;

}
