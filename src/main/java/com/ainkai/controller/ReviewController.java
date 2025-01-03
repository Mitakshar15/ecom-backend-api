package com.ainkai.controller;


import com.ainkai.dto.ReviewResponseDTO;
import com.ainkai.exceptions.ProductException;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.Review;
import com.ainkai.model.User;
import com.ainkai.request.ReviewRequest;
import com.ainkai.service.ReviewService;
import com.ainkai.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/product/reviews")
public class ReviewController {

    private UserService userService;
    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }


    @PostMapping("/create")
    public ResponseEntity<ReviewResponseDTO> createReviewHandler(@RequestBody ReviewRequest request, @RequestHeader("Authorization")String jwt, @RequestParam("rating")Double rating)throws UserException, ProductException{
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(request,user,rating);

        return new ResponseEntity<ReviewResponseDTO>(ReviewResponseDTO.fromEntity(review), HttpStatus.ACCEPTED);
    }

    @GetMapping("/{productId}")
   public ResponseEntity<List<ReviewResponseDTO>> getAllReviewsHandler(@PathVariable("productId") Long productId)throws  ProductException{

        List<Review> reviewList = reviewService.getAllProductReview(productId);
        List<ReviewResponseDTO> dtoList = ReviewResponseDTO.fromEntityToList(reviewList);
        return  new ResponseEntity<>(dtoList,HttpStatus.OK);


   }
}
