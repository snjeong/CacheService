package com.amore.api.cacheservice;

import com.amore.api.cacheservice.model.Product;
import com.amore.api.cacheservice.repository.CategoryRepository;
import com.amore.api.cacheservice.repository.ProductRepository;
import com.amore.api.cacheservice.service.CategoryService;
import com.amore.api.cacheservice.service.ProductService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CacheserviceApplication {

    @Value("${categorycache.timetoliveSeconds}")
    private long timetoliveSeconds;

    @Value("${categorycache.timetoliveSeconds}")
    private long timerintervalSeconds;

    @Value("${categorycache.maxItems}")
    private int maxItems;


    private final CategoryService categoryService;

    private final ProductService productService;

    public CacheserviceApplication(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    public static void main(String[] args) {

        SpringApplication.run(CacheserviceApplication.class, args);
    }

    @Bean
    InitializingBean loadData() {
        return () -> {
            categoryService.loadData();
            productService.loadData();

            List<Product> products = productService.getAll();

/*            for (Product item : products)
                System.out.println("data" + item.getProductName());*/

            System.out.println("categorycache.timetoliveSeconds" + timetoliveSeconds);
        };
    }

}
