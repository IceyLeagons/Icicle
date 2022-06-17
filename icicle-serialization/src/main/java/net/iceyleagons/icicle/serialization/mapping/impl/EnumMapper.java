/*
 * MIT License
 *
 * Copyright (c) 2022 IceyLeagons and Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.iceyleagons.icicle.serialization.mapping.impl;

import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.annotations.EnumSerialization;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.dto.ValueGetter;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.SerializationPropertyMapper;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
@SerializationPropertyMapper
public class EnumMapper extends PropertyMapper<Enum<?>> {

    @Override
    public Enum<?> deMap(Object object, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        if (annotations.containsKey(EnumSerialization.class)) {
            EnumSerialization enumSerialization = (EnumSerialization) annotations.get(EnumSerialization.class);

            switch (enumSerialization.value()) {
                case NAME -> {
                    return getFromName((String) object, originalType);
                }

                case ORDINAL -> {
                    return (Enum<?>) originalType.getEnumConstants()[(int) object];
                }
            }
        }

        return getFromName((String) object, originalType);
    }

    @Override
    protected ObjectValue mapCasted(Enum<?> object, Class<?> javaType, ObjectMapper context, ObjectValue old) {
        if (old.getAnnotations().containsKey(EnumSerialization.class)) {
            EnumSerialization enumSerialization = (EnumSerialization) old.getAnnotations().get(EnumSerialization.class);

            switch (enumSerialization.value()) {
                case NAME -> {
                    return old.copyWithNewValueAndType(object.name(), javaType);
                }

                case ORDINAL -> {
                    return old.copyWithNewValueAndType(object.ordinal(), javaType);
                }
            }
        }

        return old.copyWithNewValueAndType(object.name(), javaType);
    }

    @Override
    public boolean supports(Class<?> type) {
        return SerializationUtils.isEnum(type);
    }

    private static Enum<?> getFromName(String name, Class<?> type) {

        for (Object enumConstant : type.getEnumConstants()) {
            Enum<?> e = (Enum<?>) enumConstant;
            if (e.name().equals(name)) {
                return e;
            }
        }

        return null;
    }

    public enum EnumMappingType {
        NAME, ORDINAL;
    }
}
