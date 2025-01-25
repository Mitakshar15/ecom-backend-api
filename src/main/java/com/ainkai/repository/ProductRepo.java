package com.ainkai.repository;

import com.ainkai.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {


    @Query("SELECT p FROM Product p " +
            "WHERE (:category = '' OR " +
            "       p.category.name LIKE %:category% OR " +
            "       p.category.parentCategory.name LIKE %:category%) " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
            "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
            "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC, "+
            "p.createdAt DESC")
 public List<Product> filterProducts(@Param("category")String category,@Param("minPrice")Integer minPrice,@Param("maxPrice")Integer maxPrice,@Param("minDiscount") Integer minDiscount,@Param("sort") String sort);


 @Query("SELECT p FROM Product p WHERE (p.title =:title AND p.brand =:brand AND p.color=:color)")
 public  Product findProductByName(@Param("title") String title,@Param("brand")String brand,@Param("color") String color);

 @Query("SELECT p FROM Product p WHERE (p.title LIKE %:searchParam%)")
 public List<Product> findProductBySearchParam(String searchParam);


}
