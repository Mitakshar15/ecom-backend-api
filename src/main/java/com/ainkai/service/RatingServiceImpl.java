package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.ainkai.request.RatingRequest;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class RatingServiceImpl implements RatingService {


    @Override
    public Rating createRating(RatingRequest request, User user) throws ProductException {
        return null;
    }

    @Override
    public List<Rating> getProductRating(Long productId) {
        return List.of();
    }
}
