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

package net.iceyleagons.icicle.web;

import io.javalin.json.JsonMapper;
import net.iceyleagons.icicle.serialization.ObjectMapper;
import net.iceyleagons.icicle.serialization.serializers.JsonSerializer;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import net.iceyleagons.icicle.utilities.lang.Experimental;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Nov. 12, 2022
 */
@Experimental
public class JavalinIcicleJsonMapper implements JsonMapper {
    private final ObjectMapper objectMapper = new ObjectMapper(new JsonSerializer());

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> T fromJsonString(@NotNull String json, @NotNull Type targetType) {
        return (T) objectMapper.readValueFromString(json, GenericUtils.getGenericTypeClass(targetType));
    }

    @NotNull
    @Override
    public String toJsonString(@NotNull Object obj, @NotNull Type type) {
        return objectMapper.writeValueAsString(obj);
    }
}
