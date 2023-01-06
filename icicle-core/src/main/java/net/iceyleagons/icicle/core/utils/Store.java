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

package net.iceyleagons.icicle.core.utils;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Sept. 11, 2022
 * @param <K> the key
 * @param <V> the value
 */
@Getter
public abstract class Store<K, V> {

    protected final Map<K, V> elements = new Object2ObjectArrayMap<>(8);

    /**
     * Used to retrieve a value based on the key.
     *
     * @param key the key
     * @return the value or null
     */
    @Nullable
    public V get(K key) {
        return elements.get(key);
    }

    /**
     * @return the elements of the store as an array
     */
    @SuppressWarnings("unchecked")
    public V[] getElementsArray() {
        return (V[]) elements.values().toArray();
    }
}
