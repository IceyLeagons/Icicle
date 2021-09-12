package net.iceyleagons.icicle.database.schema;

import net.iceyleagons.icicle.utilities.Asserts;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CollectionSchema {

    private final String collectionName;

    private final Set<CollectionEntry> entries = new HashSet<>();
    private final Map<String, CollectionSchema> subSchema = new HashMap();

    public CollectionSchema(String collectionName) {
        Asserts.notNull(collectionName, "Collection name must not be null!");
        Asserts.isTrue(!collectionName.isEmpty(), "Collection name must not be empty!");

        this.collectionName = collectionName;
    }

    public void addSubCollectionSchema(CollectionSchema collectionSchema, String entryName) {
        Asserts.notNull(collectionSchema, "Sub collection schema must not be null!");

        this.subSchema.put(entryName, collectionSchema);
    }

    public void addIdEntry(String name, Class<?> type, Field correspondingField) {
        Asserts.notNull(collectionName, "Entry name must not be null!");
        Asserts.notNull(correspondingField, "Corresponding field must not be null!");
        Asserts.isTrue(!collectionName.isEmpty(), "Entry name must not be empty!");

        this.entries.add(new CollectionEntry(name, true, type, correspondingField));
    }

    public void addEntry(String name, Class<?> type, Field correspondingField) {
        Asserts.notNull(collectionName, "Entry name must not be null!");
        Asserts.notNull(correspondingField, "Corresponding field must not be null!");
        Asserts.isTrue(!collectionName.isEmpty(), "Entry name must not be empty!");

        this.entries.add(new CollectionEntry(name, false, type, correspondingField));
    }
}
