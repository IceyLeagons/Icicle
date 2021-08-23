package net.iceyleagons.icicle.utilities;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class ListUtils {

    public static <T> List<T> reverseList(List<T> list) {
        Collections.reverse(list);
        return list;
    }

    public static <T> LinkedList<T> reverseLinkedList(LinkedList<T> list) {
        LinkedList<T> result = new LinkedList<>();

        Iterator<T> iterator = list.descendingIterator();
        while (iterator.hasNext()) {
            result.add(iterator.next());
        }

        return result;
    }
}
