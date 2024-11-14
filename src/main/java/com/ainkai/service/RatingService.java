package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.ainkai.request.RatingRequest;

import java.util.List;

public interface RatingService {

  public Rating createRating(RatingRequest request, User user) throws ProductException;

  public List<Rating> getProductRating (Long productId);


}
