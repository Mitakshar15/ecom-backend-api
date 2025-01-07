package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddItemToCartRequest;
import com.ainkai.request.AddItemRequest;

public interface CartService {


    public Cart createCart(User user);

    public String addCartitem(Long userId, AddItemToCartRequest addItemRequest) throws ProductException;

    public Cart findUserCart(Long userId);




}
