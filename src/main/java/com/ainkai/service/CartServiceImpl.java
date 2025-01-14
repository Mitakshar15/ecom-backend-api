package com.ainkai.service;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.Product;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddItemToCartRequest;
import com.ainkai.repository.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepo cartRepo;
    private final CartItemService cartItemService;
    private final ProductService productService;

    @Override
    public Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        return cartRepo.save(cart);

    }

    @Override
    public String addCartitem(Long userId, AddItemToCartRequest addItemRequest) throws ProductException {
        Cart cart = cartRepo.findByUserId(userId);
        Product product = productService.findProductById(addItemRequest.getProductId());
        addItemRequest.setPrice(product.getDiscountedPrice());
        CartItem isPresent = cartItemService.isCartItemExists(cart,product,addItemRequest.getSize(),userId);
        if(isPresent == null){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(cart);
            cartItem.setQuantity(addItemRequest.getQuantity());
            cartItem.setUserId(userId);

            int price = addItemRequest.getQuantity() * addItemRequest.getPrice();
            cartItem.setPrice(price);
            cartItem.setSize(addItemRequest.getSize());

            CartItem createdCartItem = cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(createdCartItem);
            return "ITEM ADDED SUCCESSFULLY";
        }
        else {
            return null;
        }
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
