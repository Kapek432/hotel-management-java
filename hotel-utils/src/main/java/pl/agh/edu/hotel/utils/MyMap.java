package pl.agh.edu.hotel.utils;

import java.util.ArrayList;
import java.util.List;

/**
    * Implementation of a simple key-value map that implements Map interface.
    *
    * @param <K> type of keys
    * @param <V> type of values
    */

public class MyMap<K, V> implements Map<K, V> {

    private final List<K> keys;
    private final List<V> values;

    public MyMap() {
        keys = new ArrayList<>();
        values = new ArrayList<>();
    }

    @Override
    public boolean put(K key, V value) {
        if (key == null || value == null) {
            return false;
        }
        int index = keys.indexOf(key);
        if (index != -1) {
            values.set(index, value);
        } else {
            keys.add(key);
            values.add(value);
        }
        return true;
    }

    @Override
    public boolean remove(K key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            keys.remove(index);
            values.remove(index);
            return true;
        }
        return false;
    }

    @Override
    public V get(K key) {
        int index = keys.indexOf(key);
        if (index != -1) {
            return values.get(index);
        }
        return null;
    }

    @Override
    public List<K> keys() {
        return new ArrayList<>(keys);
    }

    @Override
    public boolean contains(K key) {
        return keys.contains(key);
    }
}
