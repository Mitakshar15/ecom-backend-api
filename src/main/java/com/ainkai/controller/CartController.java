package com.ainkai.controller;


import com.ainkai.dto.CartResponseDTO;
import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Cart;
import com.ainkai.model.User;
import com.ainkai.request.AddItemRequest;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.CartService;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<CartResponseDTO> findUserCart(@RequestHeader("Authorization") String jwt)throws UserException{

        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());

        return new ResponseEntity<>(CartResponseDTO.fromEntity(cart), HttpStatus.OK);

    }

    @PutMapping("/add")
    public  ResponseEntity<ApiResponse> addItemToCartHandler(@RequestBody AddItemRequest req,@RequestHeader("Authorization")String jwt) throws ProductException,UserException{

        User user = userService.findUserProfileByJwt(jwt);

        cartService.addCartitem(user.getId(),req);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("ITEM ADDED TO CART");
        apiResponse.setStatus(true);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);


    }



}
