package net.iceyleagons.icicle.utilities.datastores.heap.impl;

import lombok.Getter;
import net.iceyleagons.icicle.utilities.Asserts;
import net.iceyleagons.icicle.utilities.generic.GenericUtils;
import net.iceyleagons.icicle.utilities.datastores.heap.Heap;
import net.iceyleagons.icicle.utilities.datastores.heap.HeapItem;

import java.util.Objects;

public class BinaryHeap<T extends HeapItem<T>> implements Heap<T> {

    private T[] array;
    private final Class<T> clazz;

    @Getter
    private int itemCount;

    public BinaryHeap(Class<T> clazz, int heapSize) {
        Asserts.notNull(clazz, "Class must not be null!");
        Asserts.state(heapSize >= 0, "Heap size must be larger or equal to 0!");

        this.itemCount = 0;
        this.clazz = clazz;
        this.array = GenericUtils.createGenericArray(clazz, heapSize);
    }

    private void swapItems(T a, T b) {
        array[a.getHeapIndex()] = b;
        array[b.getHeapIndex()] = a;

        int ai = a.getHeapIndex();
        a.setHeapIndex(b.getHeapIndex());
        b.setHeapIndex(ai);
    }

    private void sortUpward(T item) {
        int pi = (item.getHeapIndex() - 1) / 2;

        while (true) {
            T parent = array[pi];
            if (item.compareTo(parent) > 0) {
                swapItems(item, parent);
            } else break;

            pi = (item.getHeapIndex() - 1) / 2;
        }
    }

    private void sortDownward(T item) {
        while (true) {
            int left = item.getHeapIndex() * 2 + 1;
            int right = item.getHeapIndex() * 2 + 2;
            int swap;

            if (left < itemCount) {
                swap = left;

                if (right < itemCount) {
                    if (array[left].compareTo(array[right]) < 0) {
                        swap = right;
                    }
                }

                if (item.compareTo(array[swap]) < 0) {
                    swapItems(item, array[swap]);
                } else return;
            } else return;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(T item) {
        if (itemCount + 1 >= array.length) {
            T[] newArray = GenericUtils.createGenericArray(clazz, array.length + 10);
            System.arraycopy(array, 0, newArray, 0, array.length);
            this.array = newArray;
        }

        item.setHeapIndex(itemCount);
        array[itemCount] = item;

        sortUpward(item);

        itemCount += 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(T item) {
        return Objects.equals(array[item.getHeapIndex()], item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(T item) {
        sortUpward(item);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T pop() {
        T firstItem = array[0];
        itemCount -= 1;

        array[0] = array[itemCount];
        array[0].setHeapIndex(0);
        sortDownward(array[0]);

        return firstItem;
    }

    @Override
    public int getSize() {
        return itemCount;
    }
}