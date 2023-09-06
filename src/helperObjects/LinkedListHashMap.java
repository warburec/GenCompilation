package helperObjects;

import java.util.*;

public class LinkedListHashMap <T, E> implements Map<T, E>, Iterable<T> {
    LinkedList<T> list;
    HashMap<T, E> hashMap;

    public LinkedListHashMap() {
        list = new LinkedList<>();
        hashMap = new HashMap<>();
    }

    /**
     * Creates an iterator for this map's key set
     */
    public Iterator<T> iterator() {
        return list.iterator();
    }

    public ListIterator<T> listIterator() {
        return list.listIterator();
    }

    public ListIterator<T> listIterator(int i) {
        return list.listIterator(i);
    }

    /**
     * Returns an array of this map's key set, in the order they were added
     */
    public Object[] toArray() {
        return list.toArray();
    }

    /**
     * Returns an array of this map's key set, in the order they were added
     */
    public T[] toArray(T[] a) {
        return list.toArray(a);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return hashMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return hashMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return hashMap.containsValue(value);
    }

    @Override
    public E get(Object key) {
        return hashMap.get(key);
    }

    public T getKey(int i) {
        return list.get(i);
    }

    @Override
    public E put(T key, E value) {
        E previousValue = hashMap.put(key, value);
        list.add(key);

        return previousValue;
    }

    @Override
    public void putAll(Map<? extends T, ? extends E> m) {
        hashMap.putAll(m);
        list.addAll(m.keySet());
    }

    @Override
    public void clear() {
        hashMap.clear();
        list.clear();
    }

    @Override
    public Set<T> keySet() {
        return hashMap.keySet();
    }

    @Override
    public Collection<E> values() {
        return hashMap.values();
    }

    @Override
    public Set<Entry<T, E>> entrySet() {
        return hashMap.entrySet();
    }

    @Override
    public E remove(Object key) {
        E lastValue = hashMap.remove(key);
        list.remove(key);

        return lastValue;
    }

}
