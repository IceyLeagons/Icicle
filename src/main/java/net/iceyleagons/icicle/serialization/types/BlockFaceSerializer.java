/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons (Tamás Tóth and Márton Kissik) and Contributors
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

package net.iceyleagons.icicle.serialization.types;

import net.iceyleagons.icicle.jnbt.CompoundTag;
import net.iceyleagons.icicle.jnbt.IntTag;
import net.iceyleagons.icicle.location.block.BlockUtils;
import net.iceyleagons.icicle.reflect.Reflections;
import net.iceyleagons.icicle.serialization.NBTType;
import net.iceyleagons.icicle.serialization.Serializer;
import org.bukkit.block.BlockFace;

import java.lang.reflect.Field;

/**
 * @author TOTHTOMI
 */
public class BlockFaceSerializer implements Serializer<IntTag, BlockFace> {

    @Override
    public IntTag serialize(Field field, Object object, String name) {
        BlockFace face = Reflections.get(field, BlockFace.class, object);
        return new IntTag(name, face == null ? 0 : BlockUtils.faceToNotch(face));
    }

    @Override
    public BlockFace deserialize(CompoundTag compoundTag, String name) {
        IntTag intTag = getChildTag(compoundTag, name, IntTag.class);
        return intTag == null ? null : BlockUtils.notchToFace(intTag.getValue());
    }

    @Override
    public void inject(CompoundTag compoundTag, String name, Field field, Object o) {
        try {
            BlockFace blockFace = NBTType.BLOCK_FACE.deserialize(compoundTag, name, field, BlockFace.class);
            Reflections.set(field, o, blockFace);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}