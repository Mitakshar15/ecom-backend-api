package com.ainkai.dto;


import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDTO {

    private Long id;

    private Long userId;

    private Set<CartItemResponseDTO> cartItems;


    private double totalPrice;

    private int totalItem;

    private int totalDiscountedPrice;

    private int discount;


    public static CartResponseDTO fromEntity(Cart cart){
        CartResponseDTO dto = new CartResponseDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setCartItems(CartItemResponseDTO.fromEntityToSet(cart.getCartItems()));
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setTotalItem(cart.getTotalItem());
        dto.setTotalDiscountedPrice(cart.getTotalDiscountedPrice());
        dto.setDiscount(cart.getDiscount());
        return dto;
    }

}
