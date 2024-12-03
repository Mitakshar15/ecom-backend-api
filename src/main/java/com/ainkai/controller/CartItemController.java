package com.ainkai.controller;

import com.ainkai.exceptions.CartItemException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.CartItem;
import com.ainkai.model.User;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.CartItemService;
import com.ainkai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;

    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService = cartItemService;
        this.userService = userService;
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteItemFromCartHandler(@PathVariable Long cartItemId,@RequestHeader("Authorization") String jwt)throws CartItemException, UserException{
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(),cartItemId);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setStatus(true);
        apiResponse.setMessage("ITEM REMOVED FROM CART SUCCESFULLY");
        return  new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    public  ResponseEntity<CartItem> updateItemFromCart(@PathVariable Long cartItemId ,@RequestHeader("Authorization")String jwt,@RequestBody CartItem cartItemreq) throws CartItemException,UserException{

        User user = userService.findUserProfileByJwt(jwt);

        CartItem cartItem = cartItemService.updateCartItem(user.getId(),cartItemId,cartItemreq);

        return  new ResponseEntity<>(cartItem,HttpStatus.ACCEPTED);


    }
}
