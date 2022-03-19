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

package net.iceyleagons.icicle.serialization.nbt;

import net.iceyleagons.icicle.serialization.nbt.tags.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 15, 2022
 */
public final class NBTConverter {

    public static Tag convertPrimitive(String name, Object value) {
        return convertPrimitives(value.getClass(), name, value);
    }

    public static Class<? extends Tag> getAppropriateTagClass(Class<?> javaType) {
        if (javaType.equals(String.class)) {
            return StringTag.class;
        } else if (javaType.equals(short.class) || javaType.equals(Short.class)) {
            return ShortTag.class;
        } else if (javaType.equals(long.class) || javaType.equals(Long.class)) {
            return LongTag.class;
        } else if (javaType.equals(long[].class) || javaType.equals(Long[].class)) {
            return LongArrayTag.class;
        } else if (javaType.equals(int.class) || javaType.equals(Integer.class)) {
            return IntTag.class;
        } else if (javaType.equals(int[].class) || javaType.equals(Integer[].class)) {
            return IntArrayTag.class;
        } else if (javaType.equals(float.class) || javaType.equals(Float.class)) {
            return FloatTag.class;
        } else if (javaType.equals(double.class) || javaType.equals(Double.class)) {
            return DoubleTag.class;
        } else if (javaType.equals(byte.class) || javaType.equals(Byte.class)) {
            return ByteTag.class;
        } else if (javaType.equals(byte[].class) || javaType.equals(Byte[].class)) {
            return ByteArrayTag.class;
        } else if (List.class.isAssignableFrom(javaType)) {
            return ListTag.class;
        } else if (Map.class.isAssignableFrom(javaType)) {
            return CompoundTag.class;
        } else {
            throw new IllegalArgumentException("Unsupported javaType: " + javaType.getName() + ". Is it a primitive (non-complex) type?");
        }
    }

    public static Tag convertPrimitives(Class<?> javaType, String name, Object value) {
        // Unsupported: ListTag, CompoundTag
        if (javaType.equals(String.class)) {
            return new StringTag(name, (String) value);
        } else if (javaType.equals(short.class) || javaType.equals(Short.class)) {
            return new ShortTag(name, (short) value);
        } else if (javaType.equals(long.class) || javaType.equals(Long.class)) {
            return new LongTag(name, (long) value);
        } else if (javaType.equals(long[].class) || javaType.equals(Long[].class)) {
            return new LongArrayTag(name, (long[]) value);
        } else if (javaType.equals(int.class) || javaType.equals(Integer.class)) {
            return new IntTag(name, (int) value);
        } else if (javaType.equals(int[].class) || javaType.equals(Integer[].class)) {
            return new IntArrayTag(name, (int[]) value);
        } else if (javaType.equals(float.class) || javaType.equals(Float.class)) {
            return new FloatTag(name, (float) value);
        } else if (javaType.equals(double.class) || javaType.equals(Double.class)) {
            return new DoubleTag(name, (double) value);
        } else if (javaType.equals(byte.class) || javaType.equals(Byte.class)) {
            return new ByteTag(name, (byte) value);
        } else if (javaType.equals(byte[].class) || javaType.equals(Byte[].class)) {
            return new ByteArrayTag(name, (byte[]) value);
        } else {
            throw new IllegalArgumentException("Unsupported javaType: " + javaType.getName() + ". Is it a primitive (non-complex) type?");
        }
    }

    public static ListTag getList(Class<?> listComponentType, String name, List<Object> value) {
        TagTypes type = TagTypes.getByClass(getAppropriateTagClass(listComponentType));
        List<Tag> converted = value.stream().map(v -> convertPrimitives(listComponentType, "", v)).collect(Collectors.toList());

        return new ListTag(name, type, converted);
    }
}
