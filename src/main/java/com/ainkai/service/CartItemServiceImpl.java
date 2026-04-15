package com.ainkai.service;

import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.InventoryException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.*;
import com.ainkai.repository.CartItemRepo;
import com.ainkai.user.domain.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

     private final CartItemRepo cartItemRepo;
     private final UserService userService;
     private final InventoryService inventoryService;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setPrice(cartItem.getSku().getPrice()* cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getSku().getDiscountedPrice()*cartItem.getQuantity());
        return cartItemRepo.save(cartItem);

    }

    @Override
    @Transactional
    public CartItem updateCartItemQuantity(Long user_id, Long cartItemId, Integer quantity) throws CartItemException, UserException {
        CartItem item = findCartItemById(cartItemId);
        User user = userService.findUserById(item.getUserId());
        if(user.getId().equals(user_id)){
            int newQuantity = item.getQuantity() + quantity;

            // Validate stock before updating quantity
            try {
                inventoryService.validateStock(item.getSku().getId(), newQuantity);
            } catch (InventoryException e) {
                throw new CartItemException(Constants.INSUFFICIENT_STOCK_KEY,
                        "Cannot update quantity: " + e.getMessage());
            }

            item.setQuantity(newQuantity);
            item.setPrice(item.getQuantity()* item.getSku().getPrice());
            item.setDiscountedPrice(item.getSku().getDiscountedPrice()*item.getQuantity());
        }
        else {
            throw new CartItemException(Constants.DATA_NOT_FOUND_KEY,"CART NOT UPDATED");
        }
        return cartItemRepo.save(item);
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Sku sku, Long userId) {
        return cartItemRepo.isCartItemExists(cart,sku,userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
         CartItem cartItem = findCartItemById(cartItemId);
         User user = userService.findUserById(cartItem.getUserId());
         User reqUser = userService.findUserById(userId);
         if(user.getId().equals(reqUser.getId())){
             cartItemRepo.deleteById(cartItemId);
         }
         else {
             throw new UserException(Constants.DATA_NOT_FOUND_KEY,"CANNOT DELETE ANOTHER USERS CART");
         }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepo.findById(cartItemId);
        if(opt.isPresent()){
            return  opt.get();
        }
        throw  new CartItemException(Constants.DATA_NOT_FOUND_KEY,"CART ITEM NOT FOUND WITH ID");
    }

    @Transactional
    @Override
    public void removeAllItems( Long cartId) throws CartItemException {
         cartItemRepo.removeAllItems(cartId);
    }
}
