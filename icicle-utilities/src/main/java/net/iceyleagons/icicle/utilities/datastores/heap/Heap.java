/*
 * MIT License
 *
 * Copyright (c) 2021 IceyLeagons and Contributors
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

package net.iceyleagons.icicle.utilities.datastores.heap;

public interface Heap<T extends HeapItem<T>> {

    /**
     * Adds a new item to the heap.
     *
     * @param item the item to add
     */
    void add(T item);

    /**
     * Used for checking whether an item is in the heap or not.
     *
     * @param item the item to check
     * @return true if the heap contains the item
     */
    boolean contains(T item);

    /**
     * Updates the heap starting from the specified item.
     * No need to call this after add!
     *
     * @param item the item to start from
     */
    void update(T item);

    /**
     * Removes, sorts the heap and returns the first element (before removal) of the heap.
     *
     * @return the first element
     */
    T pop(); //removes first

    /**
     * @return the size of the heap
     */
    int getSize();

}
