package com.ainkai.repository;

import com.ainkai.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CartRepo extends JpaRepository<Cart,Long> {


   @Query("SELECT c FROM Cart c Where c.user.id=:userId")
   public Cart findByUserId(@Param("userId") Long userId);

}
