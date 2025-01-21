package com.ainkai.service;

import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;
import com.ainkai.model.User;
import com.ainkai.repository.CartItemRepo;
import com.ainkai.repository.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService{

     private final CartItemRepo cartItemRepo;
     private final UserService userService;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setPrice(cartItem.getProduct().getPrice()* cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice()*cartItem.getQuantity());
        return cartItemRepo.save(cartItem);

    }

    @Override
    public CartItem updateCartItemQuantity(Long user_id, Long cartItemId, Integer quantity) throws CartItemException, UserException {
        CartItem item = findCartItemById(cartItemId);
        User user = userService.findUserById(item.getUserId());
        if(user.getId().equals(user_id)){
            item.setQuantity(item.getQuantity()+quantity);
            item.setPrice(item.getQuantity()* item.getProduct().getPrice());
            item.setDiscountedPrice(item.getProduct().getDiscountedPrice()*item.getQuantity());
        }
        else {
            throw new CartItemException("CART NOT UPDATED");
        }
        return cartItemRepo.save(item);
    }

    @Override
    public CartItem isCartItemExists(Cart cart, Product product, String size, Long userId) {
        return cartItemRepo.isCartItemExists(cart,product,size,userId);
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
             throw new UserException("CART KEY","CANNOT DELETE ANOTHER USERS CART");
         }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepo.findById(cartItemId);
        if(opt.isPresent()){
            return  opt.get();
        }
        throw  new CartItemException("CART ITEM NOT FOUND WITH ID");
    }

    @Transactional
    @Override
    public void removeAllItems( Long cartId) throws CartItemException, UserException {
         cartItemRepo.removeAllItems(cartId);
    }
}
