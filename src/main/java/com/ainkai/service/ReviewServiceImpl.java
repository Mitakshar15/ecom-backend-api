package com.ainkai.service;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import com.ainkai.model.Review;
import com.ainkai.model.User;
import com.ainkai.repository.ProductRepo;
import com.ainkai.repository.ReviewRepo;
import com.ainkai.request.RatingRequest;
import com.ainkai.request.ReviewRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {
     private ReviewRepo reviewRepo;
     private ProductRepo productRepo;
     private ProductService productService;
     private RatingService ratingService;

    public ReviewServiceImpl(ProductRepo productRepo, ProductService productService, ReviewRepo reviewRepo,RatingService ratingService) {
        this.productRepo = productRepo;
        this.productService = productService;
        this.reviewRepo = reviewRepo;
        this.ratingService = ratingService;
    }

    @Override
    public Review createReview(ReviewRequest request, User user,Double rating) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setProductId(product.getId());
        ratingRequest.setRating(rating);
        Rating savedRating =  ratingService.createRating(ratingRequest,user);
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReview(request.getReview());
        review.setCreatedAt(LocalDateTime.now());
        review.setRating(savedRating);
        return reviewRepo.save(review);
    }

    @Override
    public List<Review> getAllProductReview(Long productId) {
        return reviewRepo.getAllProductReviews(productId);
    }
}
