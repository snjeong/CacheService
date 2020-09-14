package com.amore.api.cacheservice.service;

import com.amore.api.cacheservice.cache.CacheManager;
import com.amore.api.cacheservice.model.Product;
import com.amore.api.cacheservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Purpose : 상품 정보 관리 서비스
 *  - 상품데이터 로딩, 리로딩, 캐시관리
 *
 *  case 4. Cache Optimization (Cache Miss의 처리 및 최소화 방법)
 *      1. 캐시초가화 API 제공
 *          invalidateProducts() 를 통해 캐시데이터 초기화 할 수 있다.
 *          다수의 데이터 원본의 변경시 초기화 후 조회 API 호출시 캐시데이터를 재 적재할 수 있도록 한다.
 *      2. 개별 상품정보에 대한 수정이 빈번할 수 있으므로 캐시의 유효시간을 설정함으로써 데이터원본(DB)과의 동기화 시간을 조절 할 수 있다.
 *      3. 캐시 적중 비율 API 제공
 *          서비스 운영시 캐싱적중율을 getHitRate() 메세드로 로깅하고 적정한 값을 설정하여 캐시데이터 리로딩 시점을 결정 할 수 있다.
 */
@Service
public class ProductServiceImpl implements ProductService{

    private ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {

        this.productRepository = productRepository;
    }

    @Value("${productcache.timetoliveSeconds}")
    private long timetoliveSeconds;

    @Value("${productcache.timerintervalSeconds}")
    private long timerintervalSeconds;

    @Value("${productcache.maxItems}")
    private int maxItems;

    private CacheManager<Long, Product> cache;

    @PostConstruct
    public void init() {
        this.cache
                = new CacheManager<>(timetoliveSeconds, timerintervalSeconds, maxItems);
    }

    @Override
    public List<Product> getProductsByCategory(Integer categoryGroupKey) {
        List<Product> products = new ArrayList<>();
        // cache miss 발생시 db 데이터 리로딩
        // 캐시데이터 리로딩
        if (cache.size() == 0) {
            List<Product> list = productRepository.findProductsByCategoryNo(categoryGroupKey);
            for (Product item : list)
                cache.put(item.getProductNo(), item);
        }

        for (Product product: cache.getAll()) {
            if (product.getCategoryNo() == categoryGroupKey)
                products.add(product);
        }

        /**
         * Cache Miss를 최소화 하기 위해 많은 수의 데이터 조회시 원본(DB)과 캐싱된 데이터 크기를 비교하여 캐시 리로딩처리.
         * 카테고리에 속한 상품리스트 조회에서 수행하는 것이 적절하다.
         */
        Long dbCount = productRepository.count(categoryGroupKey);
        if (products.size() != dbCount)
            return loadProductsByCategoryNo(categoryGroupKey);

        return products;
    }

    @Override
    public Product getProductByProductNo(long productNo) {

        // cache miss 발생시 db 데이터 로딩
        if (cache.get(productNo) == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("[getProductByProductNo][cache miss][key] :: ");
            sb.append(productNo);
            System.out.println(sb.toString());
            Optional<Product> product = productRepository.findById(productNo);
            if(product.isPresent())
                cache.put(product.get().getProductNo(), product.get());
        }

        return cache.get(productNo);
    }

    @Override
    public void updateProduct(Product product) {

        Optional<Product> item = productRepository.findById(product.getProductNo());
        if (item.isPresent())
            productRepository.save(product);
    }

    @Override
    public void insertProduct(Product product) {

        Optional<Product> item = productRepository.findById(product.getProductNo());
        if (!item.isPresent())
            productRepository.save(product);
    }

    @Override
    public void deleteProduct(long productId) {

        Optional<Product> item = productRepository.findById(productId);
        if (item.isPresent())
            productRepository.deleteById(productId);
    }


    @Override
    public void loadData() {
        List<Product> list = productRepository.findAll();
        for (Product item : list)
            cache.put(item.getProductNo(), item);
    }

    private List<Product> loadProductsByCategoryNo(int categoryGroupKey) {
        List<Product> list = productRepository.findProductsByCategoryNo(categoryGroupKey);
        for (Product item : list)
            cache.put(item.getProductNo(), item);

        return list;
    }

    @Override
    public double getHitRate() {
        return cache.getHitRate();
    }

    @Override
    public void invalidateProducts() {
        
        cache.clear();
    }

    @Override
    public List<Product> getAll() {

        return productRepository.findAll();
    }
}
