package com.ainkai.service;


import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;

public interface CartItemService {

    public CartItem createCartItem(CartItem cartItem);

    public CartItem updateCartItem(Long user_id,Long id,CartItem cartItem)throws CartItemException, UserException;


    public CartItem isCartItemExists(Cart cart, Product product,String size,Long userId);

    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId)throws CartItemException;


    public void removeAllItems(Long cartId)throws CartItemException,UserException;


}
