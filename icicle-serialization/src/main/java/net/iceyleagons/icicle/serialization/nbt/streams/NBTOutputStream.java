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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.GZIPOutputStream;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 15, 2022
 */
public class NBTOutputStream implements Closeable, AutoCloseable {

    private final DataOutputStream outputStream;

    public NBTOutputStream(OutputStream outputStream) throws IOException {
        this(outputStream, true);
    }

    public NBTOutputStream(OutputStream outputStream, boolean gzipped) throws IOException {
        if (gzipped) {
            outputStream = new GZIPOutputStream(outputStream);
        }

        this.outputStream = new DataOutputStream(outputStream);
    }

    public void writeTag(final Tag tag) throws IOException {
        final int type = tag.getType().getId();
        final String name = tag.getName();
        final byte[] nameBytes = name.getBytes(StandardCharsets.UTF_8);

        this.outputStream.writeByte(type);
        this.outputStream.writeShort(nameBytes.length);
        this.outputStream.write(nameBytes);

        if (tag.getType() == TagTypes.END) {
            throw new IOException("[NBT] Named TAG_End not permitted.");
        }

        writeTagData(tag);
    }

    private void writeTagData(final Tag tag) throws IOException {
        switch (tag.getType()) {
            case END -> this.writeEndTag();
            case BYTE -> this.write((ByteTag) tag);
            case SHORT -> this.write((ShortTag) tag);
            case INT -> this.write((IntTag) tag);
            case LONG -> this.write((LongTag) tag);
            case FLOAT -> this.write((FloatTag) tag);
            case DOUBLE -> this.write((DoubleTag) tag);
            case BYTE_ARRAY -> this.write((ByteArrayTag) tag);
            case STRING -> this.write((StringTag) tag);
            case LIST -> this.write((ListTag) tag);
            case COMPOUND -> this.write((CompoundTag) tag);
            case INT_ARRAY -> this.write((IntArrayTag) tag);
            case LONG_ARRAY -> this.write((LongArrayTag) tag);
        }
    }

    private void write(final ByteTag tag) throws IOException {
        this.outputStream.writeByte(tag.getValue());
    }

    private void write(final ByteArrayTag tag) throws IOException {
        final byte[] data = tag.getValue();
        this.outputStream.writeInt(data.length);
        this.outputStream.write(data);
    }

    private void write(final CompoundTag tag) throws IOException {
        for (final Tag child : tag.getValue().values()) {
            writeTag(child);
        }
        this.writeEndTag();
    }

    private void write(final ListTag tag) throws IOException {
        final TagTypes type = tag.getInternalType();
        final List<Tag> tags = tag.getValue();
        final int size = tags.size();

        this.outputStream.writeByte(type.getId());
        this.outputStream.writeInt(size);
        for (Tag value : tags) {
            writeTagData(value);
        }
    }

    private void write(final StringTag tag) throws IOException {
        final byte[] data = tag.getValue().getBytes(StandardCharsets.UTF_8);
        this.outputStream.writeShort(data.length);
        this.outputStream.write(data);
    }

    private void write(final DoubleTag tag) throws IOException {
        this.outputStream.writeDouble(tag.getValue());
    }

    private void write(final FloatTag tag) throws IOException {
        this.outputStream.writeFloat(tag.getValue());
    }

    private void write(final LongTag tag) throws IOException {
        this.outputStream.writeLong(tag.getValue());
    }

    private void write(final IntTag tag) throws IOException {
        this.outputStream.writeInt(tag.getValue());
    }

    private void write(final ShortTag tag) throws IOException {
        this.outputStream.writeShort(tag.getValue());
    }

    private void write(final IntArrayTag tag) throws IOException {
        final int[] data = tag.getValue();
        this.outputStream.writeInt(data.length);

        for (int number : data) {
            this.outputStream.writeInt(number);
        }
    }

    private void write(final LongArrayTag tag) throws IOException {
        final long[] data = tag.getValue();
        this.outputStream.writeLong(data.length);
        for (long number : data) {
            this.outputStream.writeLong(number);
        }
    }

    private void writeEndTag() throws IOException {
        this.outputStream.writeByte((byte) 0); // end tag
    }

    @Override
    public void close() throws IOException {
        this.outputStream.close();
    }
}
