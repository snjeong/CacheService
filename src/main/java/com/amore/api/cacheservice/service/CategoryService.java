package com.amore.api.cacheservice.service;

import com.amore.api.cacheservice.model.Category;

import java.util.List;

/**
 * Project : cacheservice
 * Developer : snjeong
 * Purpose :
 */
public interface CategoryService {

    // 질의 메서드
    Category findCategoryGroup(Integer categoryGroupKey);

    List<Category> getCategoryList();

    void updateCategory(Category category);

    void insertCategory(Category category);

    void deleteCategory(int categoryId);

    // 초기화 메서드
    void invalidateCategroy();

    // 캐시 히트 비율
    double getHitRate();

    void loadData();
}
