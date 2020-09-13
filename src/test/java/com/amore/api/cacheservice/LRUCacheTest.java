package com.amore.api.cacheservice;

import com.amore.api.cacheservice.cache.LRUCache;
import com.amore.api.cacheservice.cache.LRUCacheEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class LRUCacheTest {

    @Test
    public void cacheTest() {
        LRUCache<Integer, String> cache = new LRUCache<Integer, String>(5);

        for (int i = 0; i < 10; i++) {
            cache.put(i, "hi");
        }
        // entries 0-4 have already been removed
        // entries 5-9 are ordered
        System.out.println("cache = " + cache.getAll());

        System.out.println(cache.get(7));
        // entry 7 has moved to the end
        System.out.println("cache = " + cache.getAll());

        for (int i = 10; i < 14; i++) {
            cache.put(i, "hi");
        }
        // entries 5,6,8,9 have been removed (eldest entries)
        // entry 7 is at the beginning now
        System.out.println("cache = " + cache.getAll());

        cache.put(42, "meaning of life");
        // entry 7 is gone too
        System.out.println("cache = " + cache.getAll());
    }

}
