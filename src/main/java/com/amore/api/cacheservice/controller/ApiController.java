package com.amore.api.cacheservice.controller;

import com.amore.api.cacheservice.model.Category;
import com.amore.api.cacheservice.model.Product;
import com.amore.api.cacheservice.service.CategoryService;
import com.amore.api.cacheservice.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Purpose : case 1. Query
 *  	특정 카테고리에 속한 상품 리스트를 조회할 수 있어야 한다.
 *  	특정 상품에 대하여 상품명, 카테고리, 가격을 조회할 수 있어야 한다.
 */

@RestController
public class ApiController {

    private CategoryService categoryService;
    private ProductService productService;

    @Autowired
    public ApiController(CategoryService categoryService,
                         ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @ApiOperation(value = "전체 카테고리정보", notes = "", response = Category.class)
    @GetMapping("/categories")
    public ResponseMessage Categories(HttpServletRequest request) throws Exception {

        List result = categoryService.getCategoryList();

        return ResponseMessage.ok().set(result);
    }

    @ApiOperation(value = "특정 카테고리정보", notes = "특정 카테고리정보 조회", response = Category.class)
    @GetMapping("/categories/{categoryId}")
    public ResponseMessage Categories(@PathVariable("categoryId") int category,HttpServletRequest request) throws Exception {

        Category result = categoryService.findCategoryGroup(category);

        if (result == null)
            return ResponseMessage.create(ErrorCode.CACHE_MISS);

        return ResponseMessage.ok().set(result);
    }

    @ApiOperation(value = "특정 카테고리에 속한 상품리스트", notes = "상품리스트 조회", response = Product.class, responseContainer = "List")
    @GetMapping("/categories/{categoryId}/products")
    public ResponseMessage ProductsByCatetory(@PathVariable("categoryId") int category,HttpServletRequest request) throws Exception {

        ErrorCode errorCode;
        List result = productService.getProductsByCategory(category);

        if (result == null)
            return ResponseMessage.create(ErrorCode.CACHE_MISS);

        return ResponseMessage.ok().set(result);
    }

    @ApiOperation(value = "특정 상품정보 조회", notes = "상품 조회", response = Product.class)
    @GetMapping("/products/{productId}")
    public ResponseMessage Products(@PathVariable("productId") int productNo,HttpServletRequest request) throws Exception {

        ErrorCode errorCode;
        Product result = productService.getProductByProductNo(productNo);

        if (result == null) {
            errorCode = ErrorCode.getErrorCodeByApiResponse("cache_miss");
            return ResponseMessage.create(errorCode);
        }

        return ResponseMessage.ok().set(result);
    }

}
