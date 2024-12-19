package com.ainkai.dto;


import com.ainkai.model.CartItem;
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
public class CartItemResponseDTO {

    private Long id;

    private ProductResponseDTO product;

    private String size;

    private int quantity;

    private Integer price;

    private Integer discountedPrice;

    private Long userId;


    public static CartItemResponseDTO fromEntity(CartItem cartItem){
        CartItemResponseDTO cartItemDto = new CartItemResponseDTO();
        cartItemDto.setId(cartItem.getId());
        cartItemDto.setSize(cartItem.getSize());
        cartItemDto.setProduct(ProductResponseDTO.fromEntity( cartItem.getProduct()));
        cartItemDto.setQuantity(cartItem.getQuantity());
        cartItemDto.setPrice(cartItem.getPrice());
        cartItemDto.setDiscountedPrice(cartItem.getDiscountedPrice());
        cartItemDto.setUserId(cartItem.getUserId());
        return cartItemDto;
    }

    public static Set<CartItemResponseDTO> fromEntityToSet(Set<CartItem> cartItems){
     Set<CartItemResponseDTO> cartItemResponseDTOSet = new HashSet<>();
     for(CartItem cartItem : cartItems){
         cartItemResponseDTOSet.add(CartItemResponseDTO.fromEntity(cartItem));
     }
     return cartItemResponseDTOSet;
    }


}


