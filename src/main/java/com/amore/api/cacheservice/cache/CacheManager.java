package com.amore.api.cacheservice.cache;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.List;

/**
 * Purpose : 데이터 캐시 관리 및 Eviction 처리
 *  case 3. Cache Data Eviction Policy
 *      1. LRU : 가장 오랫동안 참조되지 않은 데이터 삭제처리
 *      2. TimeToLive : 데이터 유효시간을 두어 폐기처리를 하며 서비스 API를 통하지 않고 직접적인 DB 데이터변경과 같은 상황에 대비한다.
 */
public class CacheManager<K, T> {


    private long timeToLive;    // 데이터 유효기간 설정값
    private int totalCount;     // 전체 fetchContent 호출 회수
    private int hitCount;       // hit 회수

    private LRUCacheEx cacheMap; // LRU Cache 맵

    protected class CacheObject {
        public long lastAccessed = System.currentTimeMillis();
        public T value;

        protected CacheObject(T value) {
            this.value = value;
        }
    }

    public CacheManager(long timeToLive, final long timerInterval, int maxItems) {
        this.timeToLive = timeToLive * 1000;

        cacheMap = new LRUCacheEx(maxItems);

        if (this.timeToLive > 0 && timerInterval > 0) {
            /**
             * 별도의 쓰레드에 의한 유효시간이 지난 데이터 삭제처리
             */
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(timerInterval * 1000);
                        } catch (InterruptedException ex) {
                        }
                        cleanup();
                    }
                }
            });

            t.setDaemon(true);
            t.start();
        }
    }

    /**
     * 새로운 항목이 추가될때 캐시용량 초과시, LRU (least recently used) 기준에의해 삭제처리됨.
     */
    public void put(K key, T value) {
        synchronized (cacheMap) {
            cacheMap.put(key, new CacheObject(value));
        }
    }

    /**
     * 검색된 항목은 MRU (most recently used).
     */
    public T get(K key) {
        synchronized (cacheMap) {
            totalCount ++;
            CacheObject c = (CacheObject) cacheMap.get(key);

            // 지정한 컨텐츠가 없을 경우 null을 리턴
            if (c == null)
                return null;
            else {
                hitCount ++;
                c.lastAccessed = System.currentTimeMillis();
                return c.value;
            }
        }
    }

    public List<T> getAll() {

        if (cacheMap.size() == 0)
            return null;

        List<CacheObject> cacheObjects = new ArrayList<>(cacheMap.values());
        List<T> result = new ArrayList<>();

        for (CacheObject c: cacheObjects) {
            result.add(c.value);
        }

        return result;
    }

    public void remove(K key) {
        synchronized (cacheMap) {
            cacheMap.remove(key);
        }
    }

    public double getHitRate() {
        return (double)hitCount / (double)totalCount;
    }

    public int size() {
        synchronized (cacheMap) {
            return cacheMap.size();
        }
    }

    /**
     * 캐시에 설정된 유효시간(초단위)을 체크하여 삭제처리 한다.
     */
    public void cleanup() {

        long now = System.currentTimeMillis();
        ArrayList<K> deleteKey = null;

        synchronized (cacheMap) {
            Iterator<Map.Entry<K, T>> iteratorE = cacheMap.entrySet().iterator();
            deleteKey = new ArrayList<K>((cacheMap.size() / 2) + 1);
            K key = null;
            CacheObject c = null;

            while (iteratorE.hasNext()) {
                key = (K) iteratorE.next();
                c = (CacheObject) cacheMap.get(key);

                if (c != null && (now > (timeToLive + c.lastAccessed))) {
                    deleteKey.add(key);
                    System.out.println("cache deleted : " + key);
                }
            }
        }

        for (K key : deleteKey) {
            synchronized (cacheMap) {
                cacheMap.remove(key);
            }

            Thread.yield();
        }
    }

    public void clear() {
        cacheMap.clear();
    }

}
