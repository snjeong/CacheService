package com.amore.api.cacheservice.repository;

import com.amore.api.cacheservice.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    String sql =
            "SELECT ca1.CATEGORY_NO,\n" +
            "       CASE\n" +
            "            WHEN ca1.DEPTH > 1\n" +
            "                THEN concat(ca2.CATEGORY_NAME, '-', ca1.CATEGORY_NAME)\n" +
            "            ELSE\n" +
            "                ca1.CATEGORY_NAME\n" +
            "           END\n" +
            "           AS CATEGORY_NAME,\n" +
            "       ca1.PARENT_NO,\n" +
            "       ca1.DEPTH\n" +
            "FROM CATEGORY ca1\n" +
            "         LEFT JOIN CATEGORY ca2\n" +
            "                   ON ca1.PARENT_NO = ca2.PARENT_NO;";
    @Query(value = sql, nativeQuery = true)
    @Override
    List<Category> findAll();
}
