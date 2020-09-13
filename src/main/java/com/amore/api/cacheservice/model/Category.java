package com.amore.api.cacheservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Category {

    @Id
    @Column(name = "CATEGORY_NO")
    private Integer categoryNo;

    @Column(name = "CATEGORY_NAME")
    private String categoryName;

    @Column(name = "PARENT_NO")
    private Integer parentNo;

    @Column(name = "DEPTH")
    private Integer depth;


    public Integer getCategoryNo() {
        return categoryNo;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Integer getParentNo() {
        return parentNo;
    }

    public Integer getDepth() {
        return depth;
    }

}


/*
CREATE TABLE IF NOT EXISTS category (
    category_no INTEGER  PRIMARY KEY,
    category_name VARCHAR(255),
    parent_no INTEGER,
    depth INTEGER NOT NULL);
 */