package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Review;
import com.ainkai.model.User;
import com.ainkai.request.ReviewRequest;

import java.util.List;

public interface ReviewService {

    public Review createReview(ReviewRequest request, User user)throws ProductException;


    public List<Review> getAllProductReview(Long productId);



}
