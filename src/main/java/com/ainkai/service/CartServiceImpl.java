package com.ainkai.service;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Cart;
import com.ainkai.model.User;
import com.ainkai.repository.CartRepo;
import com.ainkai.request.AddItemRequest;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{

    private CartRepo cartRepo;
    private CartItemService cartItemService;


    @Override
    public Cart createCart(User user) {
        return null;
    }

    @Override
    public String addCartitem(Long userId, AddItemRequest addItemRequest) throws ProductException {
        return "";
    }

    @Override
    public Cart findUserCart(Long userId) {
        return null;
    }
}
