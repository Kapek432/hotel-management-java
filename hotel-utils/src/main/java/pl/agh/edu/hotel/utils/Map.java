package pl.agh.edu.hotel.utils;

import java.util.List;

/**
 * Generic interface representing a simple key-value map.
 *
 * @param <K> type of keys
 * @param <V> type of values
 */
public interface Map<K, V> {

    /**
     * Adds an element to the map under the given key.
     * If the key already exists, its value should be replaced.
     *
     * @param key the key (not null)
     * @param value the value associated with the key (not null)
     * @return true if the element was successfully added or updated, false otherwise
     */
    boolean put(K key, V value);

    /**
     * Removes the given key and its associated value from the map.
     *
     * @param key the key to remove
     * @return true if the key was successfully removed, false otherwise
     */
    boolean remove(K key);

    /**
     * Returns the value associated with the given key, or null if the key does not exist.
     *
     * @param key the key (not null)
     * @return the value under the key, or null if the key does not exist
     */
    V get(K key);

    /**
     * Returns a list of all keys in the map.
     *
     * @return java.util.List of keys
     */
    List<K> keys();

    /**
     * Checks whether the given key exists in the map.
     *
     * @param key the key to check
     * @return true if the key exists, false otherwise
     */
    boolean contains(K key);
}
