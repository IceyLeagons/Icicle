package net.iceyleagons.icicle.serialization.converters.builtin;

import net.iceyleagons.icicle.serialization.annotations.Converter;
import net.iceyleagons.icicle.serialization.converters.AbstractConverter;

import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 21, 2021
 */
@Converter
public class UUIDConverter extends AbstractConverter<UUID, String> {

    @Override
    public UUID fromSerialized(String serialized) throws Exception {
        return UUID.fromString(serialized);
    }

    @Override
    public String serialize(UUID toSerialize) throws Exception {
        return toSerialize.toString();
    }
}
