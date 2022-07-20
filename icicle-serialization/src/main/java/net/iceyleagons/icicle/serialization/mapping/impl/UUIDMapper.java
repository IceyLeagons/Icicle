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
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.serialization.mapping.PropertyMapper;
import net.iceyleagons.icicle.serialization.mapping.SerializationPropertyMapper;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.UUID;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 14, 2022
 */
@SerializationPropertyMapper
public class UUIDMapper extends PropertyMapper<UUID> {

    @Override
    public UUID deMap(Object object, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        return UUID.fromString((String) object);
    }

    @Override
    protected ObjectValue mapCasted(UUID object, Class<?> javaType, ObjectMapper context, ObjectValue value) {
        return value.copyWithNewValueAndType(object.toString(), String.class);
    }

    @Override
    public boolean supports(Class<?> type) {
        return UUID.class.isAssignableFrom(type);
    }
}
