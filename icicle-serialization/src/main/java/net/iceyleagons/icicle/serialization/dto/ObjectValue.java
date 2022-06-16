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

package net.iceyleagons.icicle.serialization.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.serialization.SerializationUtils;

import java.util.function.Consumer;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ObjectValue {

    private final Class<?> javaType;
    private final String key; //name
    private final Object value;

    public <T> T getValueAs(Class<T> clazz) {
        return SerializationUtils.getValueAs(clazz, value);
    }

    public boolean shouldConvert() {
        return false;
    }

    public boolean isValuePrimitiveOrString() {
        return SerializationUtils.isValuePrimitiveOrString(this.javaType);
    }

    public boolean isArray() {
        return SerializationUtils.isArray(this.javaType);
    }

    public boolean isEnum() {
        return SerializationUtils.isEnum(this.javaType);
    }

    public boolean isCollection() {
        return SerializationUtils.isCollection(this.javaType);
    }

    public boolean isMap() {
        return SerializationUtils.isMap(this.javaType);
    }

    public boolean isSubObject() {
        return SerializationUtils.isSubObject(this.javaType);
    }

}
