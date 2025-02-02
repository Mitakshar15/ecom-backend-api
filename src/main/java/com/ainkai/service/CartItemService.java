package com.ainkai.service;


import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;
import com.ainkai.model.Sku;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItemQuantity(Long user_id,Long cartItemId,Integer quantity)throws CartItemException, UserException;

    public CartItem isCartItemExists(Cart cart, Sku sku, Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId)throws CartItemException;

    public void removeAllItems(Long cartId)throws CartItemException,UserException;


}
