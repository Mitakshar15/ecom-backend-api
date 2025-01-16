package com.ainkai.service;


import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import com.ainkai.model.Review;
import com.ainkai.model.User;
import com.ainkai.model.dtos.ReviewRequest;
import com.ainkai.repository.ReviewRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ReviewServiceImpl implements ReviewService {
     private final ReviewRepo reviewRepo;
     private final ProductService productService;
     private final RatingService ratingService;

    @Override
    public Review createReview(ReviewRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        com.ainkai.model.dtos.RatingRequest ratingRequest = new com.ainkai.model.dtos.RatingRequest();
        ratingRequest.setProductId(product.getId());
        ratingRequest.setRating(request.getRating());
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
