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

package net.iceyleagons.icicle.serialization.mapping;

import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.SerializationUtils;
import net.iceyleagons.icicle.serialization.dto.ObjectValue;
import net.iceyleagons.icicle.utilities.generic.acessors.OneTypeAccessor;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Jun. 13, 2022
 */
public abstract class PropertyMapper<A> extends OneTypeAccessor<A> {

    public abstract A deMap(Object object, Class<?> originalType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations);

    public ObjectValue map(Object object, String key, Class<?> javaType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations) {
        // System.out.println("Wanted type: " + super.getATypeClass().getName());
        // System.out.println("Currnet: " + javaType.getName());

        return mapCasted(SerializationUtils.getValueAs(super.getATypeClass(), object), key, javaType, context, annotations);
    }

    protected abstract ObjectValue mapCasted(A object, String key, Class<?> javaType, ObjectMapper context, Map<Class<? extends Annotation>, Annotation> annotations);


    public abstract boolean supports(Class<?> type);

}
