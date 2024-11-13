package com.ainkai.service;

import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;

public class CartItemServiceImpl implements CartItemService{
    @Override
    public CartItem createCartItem(CartItem cartItem) {
        return null;
    }

    @Override
    public CartItem updateCartItem(Long user_id, Long id, CartItem cartItem) throws CartItemException, UserException {
        return null;
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        return null;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        return null;
    }
}
