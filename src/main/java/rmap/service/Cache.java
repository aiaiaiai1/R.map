package rmap.service;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private final Map<String, String> cache = new ConcurrentHashMap();
    private final Map<String, Long> createdAt = new ConcurrentHashMap();
    private final Timer timer = new Timer();
    private final Long cachingTime;

    public Cache(long cachingMillsTime) {
        this.cachingTime = cachingMillsTime;
    }

    public void put(String key, String value) {
        cache.put(key, value);
        createdAt.put(key, System.currentTimeMillis());
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() - createdAt.get(key) >= cachingTime) {
                    clearKey(key);
                }
            }
        }, cachingTime);
    }

    public boolean containsKey(String key) {
        return cache.containsKey(key);
    }

    public boolean containsKeyValue(String key, String value) {
        if (cache.get(key) == null) {
            return false;
        }
        return cache.get(key).equals(value);
    }

    public void clearKey(String key) {
        cache.remove(key);
        createdAt.remove(key);
    }

    public void clearKeySince(Long duration) {
        createdAt.entrySet()
                .stream()
                .filter(entry -> System.currentTimeMillis() - entry.getValue() >= duration)
                .map(Map.Entry::getKey)
                .forEach(email -> {
                    clearKey(email);
                });
    }
}
