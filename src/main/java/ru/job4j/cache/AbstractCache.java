package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<K, V> {
    protected final Map<K, SoftReference<V>> cache = new HashMap<>();

    public void put(K key, V value) {
        cache.put(key, new SoftReference<>(value));
    }

    public V get(K key) {
        V data = cache.getOrDefault(key, new SoftReference<>(null)).get();
        if (data == null) {
            data = load(key);
            put(key, data);
        }
        return data;
    }

    protected abstract V load(K key);
}
