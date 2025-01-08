package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1CartControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.ProductException;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Cart;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddItemToCartResponse;
import com.ainkai.model.dtos.CartItemResponse;
import com.ainkai.model.dtos.CartResponse;
import com.ainkai.model.dtos.AddItemToCartRequest;
import com.ainkai.model.dtos.EcomApiServiceBaseApiResponse;
import com.ainkai.repository.CartRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.service.CartItemService;
import com.ainkai.service.CartService;
import com.ainkai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserCartController implements EcomApiV1CartControllerApi {


    private final UserService userService;
    private final CartService cartService;
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final CartItemService cartItemService;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;

    public ResponseEntity<CartResponse> getUserCartHandler(@RequestHeader("Authorization")String jwt) {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        CartResponse cartResponse = mapper.toCartResponse(builder.buildSuccessApiResponse("Cart Fetched succesfully"));
        cartResponse.data(builder.buildCartDto(cart));
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    public ResponseEntity<AddItemToCartResponse> addItemToCartHandler(@RequestHeader("Authorization")String jwt, AddItemToCartRequest request) throws ProductException  {
           User user = userService.findUserProfileByJwt(jwt);
           String  isCartAdded = cartService.addCartitem(user.getId(),request);
           if (isCartAdded != null) {
               AddItemToCartResponse cartResponse =  mapper.toAddItemToCartResponse(builder.buildSuccessApiResponse("ITEM ADDED TO CART"));
               return new ResponseEntity<>(cartResponse, HttpStatus.OK);
           }
           else {
               AddItemToCartResponse cartErrorResponse = mapper.toAddItemToCartResponse(builder.buildErrorApiResponse("ITEM ALREDY EXISTS IN CART"));
               return new ResponseEntity<>(cartErrorResponse, HttpStatus.BAD_REQUEST);
           }
    }

    public ResponseEntity<CartItemResponse> removeCartItemHandler(@PathVariable("cartItemId")Long cartItemId, @RequestHeader("Authorization")String jwt) throws ProductException, CartItemException {
           User user = userService.findUserProfileByJwt(jwt);
           cartItemService.removeCartItem(user.getId(),cartItemId);
           CartItemResponse response = mapper.toCartItemResponse(builder.buildSuccessApiResponse("ITEM REMOVED"));
           return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<CartItemResponse> updateCartItemQuantityHandler(@PathVariable Long cartItemId ,@PathVariable("quantity")Integer quantity, @RequestHeader("Authorization")String jwt)throws ProductException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.updateCartItemQuantity(user.getId(),cartItemId,quantity);
        CartItemResponse response = mapper.toCartItemResponse(builder.buildSuccessApiResponse("ITEM Quantity UPDATED"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<EcomApiServiceBaseApiResponse> clearCartHandler(@RequestHeader("Authorization")String jwt) throws CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeAllItems(cartService.findUserCart(user.getId()).getId());
        EcomApiServiceBaseApiResponse response = mapper.toEcomApiServiceBaseApiResponse(builder.buildSuccessApiResponse("CART REMOVED"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
