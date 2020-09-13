package com.amore.api.cacheservice.cache;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.List;

/**
 * Purpose : LRU 알고리즘 적용을 위해 LinkedHashMap 자료구조를 활용
 *  - LinkedHashMap 은 HashMap, Double Linked List 를 사용하는 자료구조
 *  - 순서를 유지하기 위해 이중 연결 리스트를 사용
 *  - 저장된 데이터들의 순서를 유지
 */
public class LRUCacheEx<K, V> extends LinkedHashMap<K, V> {
    private final int maxEntries;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     *
     * @param initialCapacity : 쵀대 몇 개의 데이터를 보관할 것인지를 정의
     * @param loadFactor : 해시테이블의 한 버킷에 평균 몇 개의 키가 매핑되는가를 나타내는 지표
     * @param maxEntries
     */
    public LRUCacheEx(int initialCapacity,
                      float loadFactor,
                      int maxEntries) {
        super(initialCapacity, loadFactor, true);
        this.maxEntries = maxEntries;
    }

    public LRUCacheEx(int initialCapacity,
                      int maxEntries) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR, maxEntries);
    }

    public LRUCacheEx(int maxEntries) {
        this(maxEntries*4/3 + 1, maxEntries);
    }

    public LRUCacheEx(Map<? extends K, ? extends V> m,
                      int maxEntries) {
        this(m.size(), maxEntries);
        putAll(m);
    }

    public Collection<V> getAll() {
        return values();
    }


    /**
     * 캐시 Eviction 처리를 위해 LRU 알고리즘을 사용한다.
     * 새로운 Entry가 추가될때 맵에서 '가장 오래된' 즉 가장 오래전에 입력되거나 사용된 것을 삭제한다.
     */
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxEntries;
    }
}