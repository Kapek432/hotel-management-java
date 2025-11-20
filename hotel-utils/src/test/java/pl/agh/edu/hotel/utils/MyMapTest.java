package pl.agh.edu.hotel.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MyMap Tests")
class MyMapTest {

    @Test
    @DisplayName("Adding and retrieving values should work correctly")
    void testPutAndGet() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        map.put("two", 2);
        assertEquals(1, map.get("one"));
        assertEquals(2, map.get("two"));
        assertNull(map.get("three"));
    }

    @Test
    @DisplayName("Removing a key should delete it from the map")
    void testRemove() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.remove("one");
        assertNull(map.get("one"));
        assertEquals(2, map.get("two"));
        assertEquals(1, map.keys().size());
    }

    @Test
    @DisplayName("The keys method should return all keys in the map")
    void testKeys() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.put("three", 3);
        assertEquals(3, map.keys().size());
        assertTrue(map.keys().contains("one"));
        assertTrue(map.keys().contains("two"));
        assertTrue(map.keys().contains("three"));
    }

    @Test
    @DisplayName("Updating the value for an existing key should overwrite it")
    void testUpdateValue() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        map.put("one", 11);
        assertEquals(11, map.get("one"));
        assertEquals(1, map.keys().size());
    }

    @Test
    @DisplayName("Removing a non-existent key should not affect the map")
    void testRemoveNonExistentKey() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        map.remove("two");
        assertEquals(1, map.get("one"));
        assertEquals(1, map.keys().size());
    }

    @Test
    @DisplayName("An empty map should return null for get and an empty key list")
    void testEmptyMap() {
        Map<String, Integer> map = new MyMap<>();
        assertNull(map.get("one"));
        assertTrue(map.keys().isEmpty());
    }

    @Test
    @DisplayName("Contains method should correctly identify existing and non-existing keys")
    void testContains() {
        Map<String, Integer> map = new MyMap<>();
        map.put("one", 1);
        assertTrue(map.contains("one"));
        assertFalse(map.contains("two"));
    }

    @Test
    @DisplayName("Handling null keys and values should behave correctly")
    void testNullKeysAndValues() {
        Map<String, Integer> map = new MyMap<>();

        assertFalse(map.put(null, 1));
        assertFalse(map.put("one", null));

        assertNull(map.get(null));

        assertFalse(map.remove(null));

        assertFalse(map.contains(null));
    }
}
