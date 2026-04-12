package com.ainkai.repository;

import com.ainkai.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CartRepo extends JpaRepository<Cart,Long> {

   @Query("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.sku WHERE c.user.id = :userId")
   Cart findByUserId(@Param("userId") Long userId);

}
