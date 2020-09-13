package com.amore.api.cacheservice;

import com.amore.api.cacheservice.model.Category;
import com.amore.api.cacheservice.repository.CategoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.Console;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void getProductTest() {

        //Product product = new Product();
        List<Category> categorys = categoryRepository.findAll();

        for(Category item : categorys) {
            System.out.println(item.toString());
        }

        //System.out.println("category" + category.toString());
    }
}
