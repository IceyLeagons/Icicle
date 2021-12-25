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

package net.iceyleagons.icicle.utilities;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class for {@link List}s.
 *
 * @author TOTHTOMI
 * @version 1.0.0
 * @since Aug. 23, 2021
 */
public final class ListUtils {

    /**
     * Reverses the list with {@link Collections#reverse(List)}.
     * <b>The original list will be affected! aka. no copy is made</b>
     *
     * @param list the list to reverse
     * @param <T>  the type of the list
     * @return the original list
     */
    public static <T> List<T> reverseList(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    /**
     * Reverses the linked list with using {@link LinkedList#descendingIterator()}.
     * <b>The original list will not be affected! aka. a copy is made</b>
     *
     * @param list the list to reverse
     * @param <T>  the type of the list
     * @return the new, reversed linked list
     */
    public static <T> LinkedList<T> reverseLinkedList(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();

        Iterator<T> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }

    @SafeVarargs
    public static <T> List<T> mergeLists(List<T>... lists) {
        if (lists.length == 0) return Collections.emptyList();

        return Arrays.stream(lists).flatMap(Collection::stream).collect(Collectors.toList());
    }
}
