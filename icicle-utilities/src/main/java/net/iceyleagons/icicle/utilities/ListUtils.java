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
