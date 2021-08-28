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
