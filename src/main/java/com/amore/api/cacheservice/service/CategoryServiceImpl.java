package com.amore.api.cacheservice.service;

import com.amore.api.cacheservice.cache.CacheManager;
import com.amore.api.cacheservice.model.Category;
import com.amore.api.cacheservice.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Purpose : 카테고리 정보 관리 서비스
 *  - 카테고리데이터 로딩, 리로딩, 캐시관리
 *  case 4. Cache Optimization (Cache Miss의 처리 및 최소화 방법)
 *      1. 캐시초가화 API를 제공한다.
 *         원본(DB) 데이터 수정시 다시 적재를 해야 하고 실제 사용하는 시점과 다르므로 Cache Miss 발생을 최소화 하기 위함이다.
 *      2. 캐시데이터 리로딩은 캐시초가화 API 호출 후 조회 API 전체카테고리 정보조회시 하도록 한다.
 *      3. 캐시의 유효시간을 설정함으로써 데이터원본(DB)과의 동기화 시간을 조절 할 수 있다.
 */
@Service
public class CategoryServiceImpl implements CategoryService{

    @Value("${categorycache.timetoliveSeconds}")
    private long timetoliveSeconds;

    @Value("${categorycache.timerintervalSeconds}")
    private long timerintervalSeconds;

    @Value("${categorycache.maxItems}")
    private int maxItems;

    private CategoryRepository categoryRepository;
    private CacheManager<Integer, Category> cache;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    @PostConstruct
    public void init() {
        this.cache
                = new CacheManager<>(timetoliveSeconds, timerintervalSeconds, maxItems);
    }


    @Override
    public List<Category> getCategoryList() {
        // chche miss 발생시 db 데이터 로딩
        if (cache.size() == 0) {
            List<Category> list = categoryRepository.findAll();
            for (Category item : list)
                cache.put(item.getCategoryNo(), item);
        }

        List<Category> list = List.copyOf(cache.getAll());
        return list;
    }

    @Override
    public Category findCategoryGroup(Integer categoryGroupKey) {
        // chche miss 발생시 db 데이터 로딩
        if (cache.get(categoryGroupKey) == null) {
            Optional<Category> category = categoryRepository.findById(categoryGroupKey);
            if (category.isPresent())
                cache.put(category.get().getCategoryNo(), category.get());
        }

        return cache.get(categoryGroupKey);
    }

    @Override
    public void updateCategory(Category category) {

        Optional<Category> item = categoryRepository.findById(category.getCategoryNo());
        if (item.isPresent())
            categoryRepository.save(category);
    }

    @Override
    public void insertCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(int categoryId) {
        Optional<Category> item = categoryRepository.findById(categoryId);
        if (item.isPresent())
            categoryRepository.deleteById(categoryId);
    }

    // 캐시 초기화
    @Override
    public void invalidateCategroy() {

        cache.clear();
    }

    @Override
    public double getHitRate() {
        return cache.getHitRate();
    }

    @Override
    public void loadData() {
        cache.clear();
        List<Category> list = categoryRepository.findAll();
        for (Category item : list)
            cache.put(item.getCategoryNo(), item);
    }
}
