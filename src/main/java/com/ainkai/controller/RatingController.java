package com.ainkai.controller;


import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.ainkai.request.RatingRequest;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.ProductService;
import com.ainkai.service.RatingService;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/ratings")
public class RatingController {


    private RatingService ratingService;

    private ProductService productService;

    private UserService userService;

    public RatingController(ProductService productService, RatingService ratingService, UserService userService) {
        this.productService = productService;
        this.ratingService = ratingService;
        this.userService = userService;
    }


    @GetMapping("/{productId}")
    public ResponseEntity<List<Rating>>getAllRatingHandler(@PathVariable("productId") Long productId){

        List<Rating> ratingList = ratingService.getProductRating(productId);
        return new ResponseEntity<>(ratingList, HttpStatus.OK);

    }

    @PostMapping("/create/{productId}/{rating}")
    public ResponseEntity<ApiResponse> createRatingHandler(@PathVariable("productId")Long productId, @PathVariable("rating")double rating, @RequestHeader("Authorization")String jwt) throws ProductException, UserException {
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setRating(rating);
        ratingRequest.setProductId(productId);
        User user = userService.findUserProfileByJwt(jwt);
        ratingService.createRating(ratingRequest,user);

        ApiResponse response = new ApiResponse();
        response.setMessage("RATING ADDED SUCCESFULLY");
        response.setStatus(true);

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
}
