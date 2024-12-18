package com.ainkai.repository;

import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepo extends JpaRepository<Rating,Long> {


    @Query("SELECT r FROM  Rating r WHERE r.product.id = :productId ")
    public List<Rating> geAllProductRatings(@Param("productId")Long productId);

    @Query("SELECT r FROM Rating r WHERE r.product.id=:productId and r.user.id=:userId")
    public Rating getProductRatingById(@Param("productId") Long productId,@Param("userId")Long userId);

}
