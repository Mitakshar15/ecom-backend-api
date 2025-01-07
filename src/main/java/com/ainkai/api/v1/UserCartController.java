package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1CartControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Cart;
import com.ainkai.model.User;
import com.ainkai.model.dtos.CartResponse;
import com.ainkai.repository.CartRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.service.CartService;
import com.ainkai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserCartController implements EcomApiV1CartControllerApi {


    private final UserService userService;
    private final CartService cartService;
    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;

    public ResponseEntity<CartResponse> getUserCartHandler(@RequestHeader("Authorization")String jwt) {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        CartResponse cartResponse = mapper.toCartResponse(builder.buildSuccessApiResponse("Cart Fetched succesfully"));
        cartResponse.data(builder.buildCartDto(cart));
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

}
