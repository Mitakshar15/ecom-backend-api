/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.builder;


import com.ainkai.api.utils.BaseApiResponse;
import com.ainkai.api.utils.Metadata;
import com.ainkai.api.utils.Status;
import com.ainkai.config.JwtProvider;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.*;
import com.ainkai.model.dtos.*;
import com.ainkai.user.domain.Constants;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ApiResponseBuilder {
    private final Tracer tracer;
    private final JwtProvider jwtProvider;
    private final EcomApiUserMapper mapper;


    public BaseApiResponse buildSuccessApiResponse(String statusMessage) {
        return new BaseApiResponse()
                .metadata(new Metadata().timestamp(Instant.now())
                        .traceId(null != tracer.currentSpan() ? tracer.currentSpan().context().traceId() : ""))
                .status(new Status().statusCode(HttpStatus.OK.value()).statusMessage(statusMessage)
                        .statusMessageKey(Constants.RESPONSE_MESSAGE_KEY_SUCCESS));
    }

    public BaseApiResponse buildErrorApiResponse(String statusMessage) {
        return new BaseApiResponse()
                .metadata(new Metadata().timestamp(Instant.now())
                        .traceId(null != tracer.currentSpan() ? tracer.currentSpan().context().traceId() : ""))
                .status(new Status().statusCode(HttpStatus.NOT_FOUND.value()).statusMessage(statusMessage)
                        .statusMessageKey(Constants.RESPONSE_MESSAGE_KEY_ERROR));
    }

    public AuthResponseDto buildAuthResponseDto(Authentication authentication){
        String token = jwtProvider.generateToken(authentication);
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setJwt(token);
        authResponseDto.setMessage("Sign Up Success");
        return authResponseDto;

    }

    public CartDto buildCartDto(Cart cart){
        return mapper.toCartDto(cart);
    }

    public List<ProductDto> buildProductDtoList(List<Product> products) {
        return products.stream()
                .map(mapper::toProductDto)
                .collect(Collectors.toList());
    }

    public OrderDto buildOrderDto(Order order) {
        return mapper.toOrderDto(order);
    }

    public List<RatingDto> buildRatingDtoList(List<Rating> ratings) {
        return ratings.stream().map(mapper::toRatingDto).collect(Collectors.toList());
    }

    public List<ReviewDto> buildReviewDtoList(List<Review> reviews) {
        List<ReviewDto> reviewDto = new ArrayList<>();
        for(Review review : reviews) {
            ReviewDto dto = mapper.toReviewDto(review);
            dto.setUserName(review.getUser().getFirstName()+" "+review.getUser().getLastName());
            dto.setRating(review.getRating().getRating());
            reviewDto.add(dto);
        }
        return reviewDto;
    }
}
