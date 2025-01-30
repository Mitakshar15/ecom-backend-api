package com.ainkai.service;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.*;
import com.ainkai.model.dtos.AddItemToCartRequest;
import com.ainkai.repository.CartRepo;
import com.ainkai.repository.SkuRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final SkuRepository skuRepository;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepo.save(cart);

    }

    @Override
    public String addCartitem(Long userId,@Valid AddItemToCartRequest addItemRequest) throws ProductException {
        Cart cart = cartRepo.findByUserId(userId);
        Sku sku = skuRepository.findById(addItemRequest.getSkuId()).get();
        addItemRequest.setPrice(sku.getDiscountedPrice());
        CartItem isPresent = cartItemService.isCartItemExists(cart,sku.getProduct(),addItemRequest.getSize(),userId);
        if(isPresent==null) {
            CartItem cartItem = new CartItem();
            cartItem.setSku(sku);
            cartItem.setCart(cart);
            cartItem.setQuantity(addItemRequest.getQuantity());
            cartItem.setUserId(userId);
            int price = addItemRequest.getPrice() * addItemRequest.getQuantity();
            cartItem.setPrice(price);
            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            cartRepo.save(cart);
            return "ITEM ADDED SUCCESSFULLY";
        }
        else {
            return null;
        }
//        Cart cart = cartRepo.findByUserId(userId);
//        Product product = productService.findProductById(addItemRequest.getProductId());
//        addItemRequest.setPrice(product.getDiscountedPrice());
//        CartItem isPresent = cartItemService.isCartItemExists(cart,product,addItemRequest.getSize(),userId);
//        if(isPresent == null){
//            CartItem cartItem = new CartItem();
//            cartItem.setProduct(product);
//            cartItem.setCart(cart);
//            cartItem.setQuantity(addItemRequest.getQuantity());
//            cartItem.setUserId(userId);
//
//            int price = addItemRequest.getQuantity() * addItemRequest.getPrice();
//            cartItem.setPrice(price);
//            cartItem.setSize(addItemRequest.getSize());
//
//            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
//            cart.getCartItems().add(createdCartItem);
//            return "ITEM ADDED SUCCESSFULLY";
//        }
//        else {
//            return null;
//        }
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepo.findByUserId(userId);
        int totalPrice = 0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartItem : cart.getCartItems()){
            totalPrice = totalPrice+ cartItem.getPrice();
            totalDiscountedPrice = totalDiscountedPrice + cartItem.getDiscountedPrice();
        }
        totalItem = cart.getCartItems().size();
        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(totalPrice-totalDiscountedPrice);

         return cartRepo.save(cart);

    }
}
