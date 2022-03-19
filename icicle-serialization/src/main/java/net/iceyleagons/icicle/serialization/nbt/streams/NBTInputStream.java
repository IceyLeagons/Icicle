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

package net.iceyleagons.icicle.serialization.nbt.streams;

import net.iceyleagons.icicle.serialization.nbt.Tag;
import net.iceyleagons.icicle.serialization.nbt.TagTypes;
import net.iceyleagons.icicle.serialization.nbt.tags.*;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 15, 2022
 */
public class NBTInputStream implements Closeable, AutoCloseable {

    private final DataInputStream inputStream;

    public NBTInputStream(InputStream inputStream) throws IOException {
        this(inputStream, true);
    }

    public NBTInputStream(InputStream inputStream, boolean gzipped) throws IOException {
        if (gzipped) {
            inputStream = new GZIPInputStream(inputStream);
        }

        this.inputStream = new DataInputStream(inputStream);
    }

    public Tag readTag() throws IOException {
        return readTag(0);
    }

    private Tag readTag(final int depth) throws IOException {
        final int typeId = this.inputStream.readByte() & 0xFF;

        String name = "";
        if (typeId != TagTypes.END.getId()) {
            final int len = this.inputStream.readShort() &  0xFFFF;
            final byte[] data = new byte[len];

            this.inputStream.readFully(data);
            name = new String(data, StandardCharsets.UTF_8);
        }
        return readData(TagTypes.getById(typeId), name, depth);
    }

    private Tag readData(final TagTypes type, final String name, int depth) throws IOException {
        switch (type) {
            case END -> {
                if (depth == 0) throw new IOException("[NBT] TAG_End found without a TAG_Compound/TAG_List preceding it.");
                return new EndTag();
            }
            case BYTE -> {
                return new ByteTag(name, this.inputStream.readByte());
            }
            case SHORT -> {
                return new ShortTag(name, this.inputStream.readShort());
            }
            case INT -> {
                return new IntTag(name, this.inputStream.readInt());
            }
            case LONG -> {
                return new LongTag(name, this.inputStream.readLong());
            }
            case FLOAT -> {
                return new FloatTag(name, this.inputStream.readFloat());
            }
            case DOUBLE -> {
                return new DoubleTag(name, this.inputStream.readDouble());
            }
            case BYTE_ARRAY -> {
                return new ByteArrayTag(name, readByte(this.inputStream));
            }
            case STRING -> {
                return new StringTag(name, new String(readByte(this.inputStream), StandardCharsets.UTF_8));
            }
            case LIST -> {
                int childType = this.inputStream.readInt();
                int length = this.inputStream.readInt();

                final List<Tag> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    final Tag tag = readData(TagTypes.getById(childType), "", depth + 1);
                    if (tag instanceof EndTag) {
                        throw new IOException("[NBT] TAG_End is not permitted in a list.");
                    }
                    list.add(tag);
                }

                return new ListTag(name, TagTypes.getById(childType), list);
            }
            case COMPOUND -> {
                final Map<String, Tag> map = new HashMap<>();
                while (true) {
                    final Tag tag = readTag(depth + 1);
                    if (tag instanceof EndTag) {
                        break;
                    }

                    map.put(tag.getName(), tag);
                }

                return new CompoundTag(name, map);
            }
            case INT_ARRAY -> {
                int length = this.inputStream.readInt();
                final int[] array = new int[length];

                for (int i = 0; i < length; i++) {
                    array[i] = this.inputStream.readInt();
                }

                return new IntArrayTag(name, array);
            }
            case LONG_ARRAY -> {
                int length = this.inputStream.readInt();
                final long[] array = new long[length];

                for (int i = 0; i < length; i++) {
                    array[i] = this.inputStream.readLong();
                }

                return new LongArrayTag(name, array);
            }
            default -> {
                return null;
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.inputStream.close();
    }

    private static byte[] readByte(DataInputStream inputStream) throws IOException {
        int length = inputStream.readInt();
        byte[] data = new byte[length];
        inputStream.readFully(data);

        return data;
    }
}
