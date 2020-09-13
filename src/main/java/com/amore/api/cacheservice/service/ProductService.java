package com.amore.api.cacheservice.service;

import com.amore.api.cacheservice.model.Product;

import java.util.List;

/**
 * Project : cacheservice
 * Developer : snjeong
 * Purpose :
 */
public interface ProductService {

    // 특정 카테고리에 속한 상품 리스트를 조회
    List<Product> getProductsByCategory(Integer categoryGroupKey);

    // 상품정보 조회
    Product getProductByProductNo(long productNo);

    // 상품정보 업데이트
    void updateProduct(Product product);

    // 상품정보 추가
    void insertProduct(Product product);

    // 상품정보 삭제
    void deleteProduct(long productId);

    // 초기 데이터 로딩
    void loadData();

    // 캐시 히트 비율
    public double getHitRate();
    
    // 초기화 메서드
    void invalidateProducts();

    List<Product> getAll();
}
