package helper_objects;

import java.util.*;

/**
 * An Iterator for the key set of a LinkedListHashMap
 */
public class LinkedMapIterator <T, E> implements Iterator<T> {
    LinkedListHashMap<T, E> linkedMap;
    int position;

    public LinkedMapIterator(LinkedListHashMap<T, E> linkedMap) {
        this.linkedMap = linkedMap;
    }

    @Override
    public boolean hasNext() {
        return position < linkedMap.size();
    }

    @Override
    public T next() {
        T item = linkedMap.getKey(position);
        position++;

        return item;
    }

    /**
     * Adds an entry to the map. Allows iteration to continue when appending
     * @param key A key to be added
     * @param value A value to be added
     */
    public void add(T key, E value) {
        linkedMap.put(key, value);
    }
}