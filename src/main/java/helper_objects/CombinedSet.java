package helper_objects;

import java.util.*;

/**
 * Note: This is not a true composite set as elements may be repeated between sub sets
 * This class is useful to combine sets whilst maintaining reference to each original set
 */
public class CombinedSet <T> implements Set<T> {
    private Set<Set<T>> sets = new HashSet<>();

    public CombinedSet() {}

    public CombinedSet(Set<T> set) {
        sets.add(set);
    }

    public void addSet(Set<T> set) {
        sets.add(set);
    }

    public void addValue(T value) {
        sets.add(Set.of(value));
    }

    public Set<T> getValue() {
        return combineSets();
    }

    private Set<T> combineSets() {
        Set<T> tokens = new HashSet<>();

        for (Set<T> set : sets) {
            tokens.addAll(set);
        }

        return tokens;
    }

    @Override
    public int size() {
        int size = 0;

        for(Set<T> set : sets) {
            size += set.size();
        }

        return size;
    }

    @Override
    public boolean isEmpty() {
        return sets.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        for(Set<T> set : sets) {
            if(set.contains(o)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Iterator<T> iterator() {
        Set<T> allVals = new HashSet<>();

        for (Set<T> set : sets) {
            allVals.addAll(set);
        }

        return allVals.iterator();
    }

    @Override
    public Object[] toArray() {
        Object[] array = new Object[size()];
        int ind = 0;

        for(Set<T> set : sets) {
            for(T object : set) {
                array[ind] = object;
                ind++;
            }
        }

        return array;
    }

    /**
     * Type E must be a supertype of T
     */
    @Override
    @SuppressWarnings("unchecked") //Unwanted
    public <E> E[] toArray(E[] a) {
        int ind = 0;

        for(Set<T> set : sets) {
            for(T object : set) {
                a[ind] = (E)object;
                ind++;
            }
        }

        return a;
    }

    @Override
    public boolean add(T e) {
        Set<T> newSet = new HashSet<T>();
        newSet.add(e);

        return sets.add(newSet);
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = false;

        for (Set<T> set : sets) {
            if(set.remove(o)) {
                removed = true;
            }
        }

        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for(Object object : c) {
            if(!contains(object)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return sets.add(new HashSet<>(c));
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Unimplemented method 'retainAll'");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = false;

        for(Object object : c) {
            if(remove(object)) {
                changed = true;
            }
        }

        return changed;
    }

    @Override
    public void clear() {
        sets.clear();
    }
}