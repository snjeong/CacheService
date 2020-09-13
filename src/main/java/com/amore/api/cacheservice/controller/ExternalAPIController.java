package com.amore.api.cacheservice.controller;

import com.amore.api.cacheservice.model.Category;
import com.amore.api.cacheservice.model.Product;
import com.amore.api.cacheservice.service.CategoryService;
import com.amore.api.cacheservice.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Purpose : case 5. 원본 데이터에 대한 ADD / DELETE / UPDATE
 *   특정 카테고리명을 변경할 수 있어야 한다.
 *   특정 상품명을 변경할 수 있어야 한다.
 *   특정 상품의 가격을 변경할 수 있어야 한다.
 */
@RestController
public class ExternalAPIController {

    private CategoryService categoryService;
    private ProductService productService;

    @Autowired
    public ExternalAPIController(CategoryService categoryService,
                         ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @ApiOperation(value = "카테고리 정보 변경", notes = "", response = String.class)
    @PostMapping("/categories")
    public ResponseMessage updateCategories(@RequestBody Category category) throws Exception {

        categoryService.updateCategory(category);

        return ResponseMessage.ok();
    }

    @ApiOperation(value = "카테고리 정보 추가", notes = "", response = String.class)
    @PutMapping("/categories")
    public ResponseMessage insertCategories(@RequestBody Category category) throws Exception {

        categoryService.insertCategory(category);

        return ResponseMessage.ok();
    }

    @ApiOperation(value = "카테고리 정보 삭제", notes = "", response = String.class)
    @DeleteMapping("/categories/{categoryId}")
    public ResponseMessage deleteCategories(@PathVariable("categoryId") int categoryId) throws Exception {

        categoryService.deleteCategory(categoryId);

        return ResponseMessage.ok();
    }

    @ApiOperation(value = "상품 정보 변경", notes = "", response = String.class)
    @PostMapping("/products")
    public ResponseMessage updateProducts(@RequestBody Product product) throws Exception {

        productService.updateProduct(product);

        return ResponseMessage.ok();
    }

    @ApiOperation(value = "상품 정보 변경", notes = "", response = String.class)
    @PutMapping("/products")
    public ResponseMessage insertProducts(@RequestBody Product product) throws Exception {

        productService.insertProduct(product);

        return ResponseMessage.ok();
    }

    @ApiOperation(value = "상품 정보 변경", notes = "", response = String.class)
    @DeleteMapping("/products/{productId}")
    public ResponseMessage deleteProducts(@PathVariable("productId") int productId) throws Exception {

        productService.deleteProduct(productId);

        return ResponseMessage.ok();
    }
}
