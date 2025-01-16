package com.ainkai.service;

import com.ainkai.exceptions.ProductException;
import com.ainkai.model.Product;
import com.ainkai.model.Rating;
import com.ainkai.model.User;
import com.ainkai.model.dtos.RatingRequest;
import com.ainkai.repository.RatingRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {


    private final RatingRepo ratingRepo;
    private final ProductService productService;

    @Override
    public Rating createRating(RatingRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(request.getRating());
        rating.setCreatedAt(LocalDateTime.now());
        return ratingRepo.save(rating);
    }

    @Override
    public List<Rating> getProductRating(Long productId) {
      return ratingRepo.geAllProductRatings(productId);
    }
}
