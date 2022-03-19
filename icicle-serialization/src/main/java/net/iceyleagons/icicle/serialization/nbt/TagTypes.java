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

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.iceyleagons.icicle.serialization.nbt.tags.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 15, 2022
 */
@Getter
@RequiredArgsConstructor
public enum TagTypes {

    // Fun fact: don't mess with the ids

    END(0, "TAG_End", EndTag.class),
    BYTE(1, "TAG_Byte", ByteTag.class),
    SHORT(2, "TAG_Short", ShortTag.class),
    INT(3, "TAG_Int", IntTag.class),
    LONG(4, "TAG_Long", LongTag.class),
    FLOAT(5, "TAG_Float", FloatTag.class),
    DOUBLE(6, "TAG_Double", DoubleTag.class),
    BYTE_ARRAY(7, "TAG_Byte_Array", ByteArrayTag.class),
    STRING(8, "TAG_String", StringTag.class),
    LIST(9, "TAG_List", ListTag.class),
    COMPOUND(10, "TAG_Compound", CompoundTag.class),
    INT_ARRAY(11, "TAG_Int_Array", IntArrayTag.class),
    LONG_ARRAY(12, "TAG_Long_Array", LongArrayTag.class)
    ;

    private final int id;
    private final String name;
    private final Class<? extends Tag> clazz;

    public static TagTypes getById(int id) {
        return Arrays.stream(values())
                .filter(t -> t.id == id)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid TagType id: " + id));
    }

    public static TagTypes getByClass(final Class<?> clazz) {
        return Arrays.stream(values())
                .filter(t -> t.clazz.equals(clazz))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid TagType clazz: " + clazz));
    }

    public static String getTypeName(final Class<? extends Tag> clazz) {
        return getByClass(clazz).name;
    }
}
