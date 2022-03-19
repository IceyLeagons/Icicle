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

package net.iceyleagons.icicle.serialization.nbt.tags;

import lombok.EqualsAndHashCode;
import net.iceyleagons.icicle.serialization.nbt.Tag;
import net.iceyleagons.icicle.serialization.nbt.TagTypes;

import java.util.HashMap;
import java.util.Map;

/**
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Mar. 15, 2022
 */
@EqualsAndHashCode(callSuper = true)
public class CompoundTag extends Tag {

    private final Map<String, Tag> value;

    public CompoundTag(final String name) {
        this(name, new HashMap<>());
    }

    public CompoundTag(final String name, final Map<String, Tag> value) {
        super(name, TagTypes.COMPOUND);
        this.value = value;
    }

    public void putTag(Tag tag) {
        putTag(tag.getName(), tag);
    }

    public void putTag(String name, Tag value) {
        this.value.put(name, value);
    }

    public <T> T getTag(String name, Class<T> type) {
        if (!this.value.containsKey(name)) return null;

        Tag tag = this.value.get(name);
        if (!type.isInstance(tag)) {
            throw new IllegalStateException(name + " tag is not of tag type " + type.getName());
        }

        return type.cast(tag);
    }

    @Override
    public Map<String, Tag> getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(value.size()).append(" entries\r\n{\r\n");
        for (final Map.Entry<String, Tag> entry : value.entrySet()) {
            sb.append("   ").append(entry.getValue().toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        }
        sb.append("}");

        return super.getToString(sb.toString());
    }
}
