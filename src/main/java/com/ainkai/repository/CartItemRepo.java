package com.ainkai.repository;

import com.ainkai.exceptions.CartItemException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface CartItemRepo extends JpaRepository<CartItem,Long> {

    //NOTE:: In the @Query() we must use Entity Name and param names only, DO NOT USE TABLE NAME AND COLUMNS
    @Query("SELECT ci from CartItem ci Where ci.cart = :cart And ci.product=:product And ci.size=:size And ci.userId = :userId")
    public CartItem isCartItemExists(@Param("cart") Cart cart, @Param("product")Product product, @Param("size") String size, @Param("userId") Long userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CartItem c Where c.cart.id =:cartId")
    public void removeAllItems(@Param("cartId")Long cartId);


}
