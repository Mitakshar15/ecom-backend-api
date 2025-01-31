package com.ainkai.repository;

import com.ainkai.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

     @Query("SELECT p FROM Product p LEFT JOIN p.skus s " +
            "WHERE (:category = '' OR " +
            "       p.category.name LIKE %:category% OR " +
            "       p.category.parentCategory.name LIKE %:category%) " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (s.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
            "AND (:minDiscount IS NULL OR s.discountPercent >= :minDiscount) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_low' THEN s.discountedPrice END ASC, " +
            "CASE WHEN :sort = 'price_high' THEN s.discountedPrice END DESC, "+
            "p.createdAt DESC")
 public List<Product> filterProducts(@Param("category")String category,@Param("minPrice")Integer minPrice,@Param("maxPrice")Integer maxPrice,@Param("minDiscount") Integer minDiscount,@Param("sort") String sort);

 @Query("SELECT p FROM Product p WHERE (p.title LIKE %:searchParam%)")
 public List<Product> findProductBySearchParam(String searchParam);

 boolean existsProductByTitleAndBrand(String title, String brand);

 Product findProductByTitleAndBrand(String title, String brand);
}
