package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1ProductControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.ainkai.model.dtos.*;
import com.ainkai.repository.CartRepo;
import com.ainkai.repository.ProductRepo;
import com.ainkai.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserProductController implements EcomApiV1ProductControllerApi {

    private final ProductService productService;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;
    private final RatingService ratingService;
    private final UserService userService;
    private final ReviewService reviewService;


    public ResponseEntity<MultipleProductResponse> findProductByCategoryHandler(@RequestParam String category, @RequestParam List<String> color, @RequestParam List<String> size, @RequestParam Integer minPrice, @RequestParam Integer maxPrice, @RequestParam Integer minDiscount, @RequestParam String sort, @RequestParam String stock, @RequestParam Integer pageNumber, @RequestParam Integer pageSize){
        MultipleProductResponse response = mapper.toMultipleProductResponse(builder.buildSuccessApiResponse("FIND FILTERED PRODUCTS SUCCESS"));
        response.products(builder.buildProductDtoList(productService.getAllFilteredProducts(category,color,size,minPrice,maxPrice,minDiscount,sort,stock,pageNumber,pageSize)));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ProductResponse> getProductByIdHandler(@PathVariable("productId")Long productId){
        Product product = productService.findProductById(productId);
        ProductResponse productResponse = mapper.toProductResponse(builder.buildSuccessApiResponse("GET PRODUCT SUCCESS"));
        productResponse.product(mapper.toProductDto(product));
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    //TODO: Controller To Handle Search Products


    public ResponseEntity<RatingResponse> getProductRatingHandler(@PathVariable("productId")Long productId){
        List<Rating> ratings = ratingService.getProductRating(productId);
        RatingResponse response = mapper.toRatingResponse(builder.buildSuccessApiResponse("GET PRODUCT RATING SUCCESS"));
        response.ratings(builder.buildRatingDtoList(ratings));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<EcomApiServiceBaseApiResponse> createRatingHandler(@RequestHeader("Authorization")String jwt,RatingRequest request){
        User user = userService.findUserProfileByJwt(jwt);
        ratingService.createRating(request, user);
        return new ResponseEntity<>(mapper.toEcomApiServiceBaseApiResponse(builder.buildSuccessApiResponse("Rating Added")),HttpStatus.OK);
    }

    public ResponseEntity<EcomApiServiceBaseApiResponse> createReviewHandler(@RequestHeader("Authorization")String jwt,ReviewRequest request){
        User user = userService.findUserProfileByJwt(jwt);
        Product product = productService.findProductById(request.getProductId());
        reviewService.createReview(request, user);
        EcomApiServiceBaseApiResponse response = mapper.toEcomApiServiceBaseApiResponse(builder.buildSuccessApiResponse("Review Added"));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<GetAllReviewResponse> getAllReviewsHandler(@PathVariable("productId")Long productId){
           List<ReviewDto> reviewDtos = builder.buildReviewDtoList(reviewService.getAllProductReview(productId));
           GetAllReviewResponse response = mapper.toGetAllReviewResponse(builder.buildSuccessApiResponse("GET ALL REVIEW SUCCESS"));
           response.reviews(reviewDtos);
           return new ResponseEntity<>(response, HttpStatus.OK);
    }





}
