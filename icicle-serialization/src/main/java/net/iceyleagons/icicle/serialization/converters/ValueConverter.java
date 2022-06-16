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

package net.iceyleagons.icicle.serialization.converters;

import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.utilities.generic.acessors.TwoTypeAccessor;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 16, 2022
 */
public abstract class ValueConverter<FIELD, SER> extends TwoTypeAccessor<FIELD, SER> {

    protected abstract SER convertToSerializedValue(FIELD input);

    protected abstract FIELD convertToObjectValue(SER serialized);

    public Object convert(Object input, boolean serializing) {
        if (serializing) {
            return convertToSerializedValue(SerializationUtils.getValueAs(getATypeClass(), input));
        }

        return convertToObjectValue(SerializationUtils.getValueAs(getBTypeClass(), input));
    }

    public boolean supports(Class<?> fieldType) {
        return super.getATypeClass().isAssignableFrom(fieldType);
    }
}
