package com.amore.api.cacheservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @Column(name = "product_no")
    private Long productNo;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "category_no")
    private Integer categoryNo;

    public Long getProductNo() {
        return productNo;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public Integer getCategoryNo() {
        return categoryNo;
    }
}


/*
    CREATE TABLE IF NOT EXISTS product (
    product_no BIGINT PRIMARY KEY,
    brand_name VARCHAR(255),
    product_name VARCHAR(255),
    product_price DECIMAL(19,2),
    category_no INTEGER);

 */