/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.mapper;

import com.ainkai.api.utils.BaseApiResponse;
import com.ainkai.model.*;
import com.ainkai.model.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper (componentModel = "spring")
public interface EcomApiUserMapper {


 @Mapping(ignore = true,target = "id")
 @Mapping(ignore = true,target = "addresses")
 @Mapping(ignore = true,target = "paymentInformation")
 @Mapping(ignore = true,target = "ratings")
 @Mapping(ignore = true,target = "reviews")
 @Mapping(ignore = true,target = "createdAt")
 @Mapping(ignore = true,target = "password")
 User toUserEntity(SignUpRequest request);

 SignUpResponse toSignUpResponse(BaseApiResponse response);

 SignInResponse toSignInResponse(BaseApiResponse response);

 GetProfileResponse toGetProfileResponse(BaseApiResponse response);

 UserResponseDto toUserResponseDto(User user);

 EditProfileResponse toEditProfileResponse(BaseApiResponse response);

 Address toAddressEntity(AddressRequest request);

 AddNewAddressResponse toAddNewAddressResponse(BaseApiResponse response);

 DeleteAddressResponse toDeleteAddressResponse(BaseApiResponse response);

 EditAddressResponse toEditAddressResponse(BaseApiResponse response);

 GetAllAddressResponse toGetAllAddressResponse(BaseApiResponse response);

 List<AddressDto> toAddressDtoList(List<Address> addressList);

 CartResponse toCartResponse(BaseApiResponse response);

 CartDto toCartDto(Cart cart);

 AddItemToCartResponse toAddItemToCartResponse(BaseApiResponse response);

 MultipleProductResponse toMultipleProductResponse(BaseApiResponse response);

 ProductDto toProductDto(Product product);

 ProductResponse toProductResponse(BaseApiResponse response);

 CartItemResponse toCartItemResponse(BaseApiResponse response);

 EcomApiServiceBaseApiResponse toEcomApiServiceBaseApiResponse(BaseApiResponse response);

 OrderDto toOrderDto(Order order);

 OrderResponse toOrderResponse(BaseApiResponse response);

 Address toAddressEntity(AddressDto addressDto);

 List<OrderDto> toOrderDtoList(List<Order> orderList);

 OrderHistoryResponse toOrderHistoryResponse(BaseApiResponse response);

 RatingDto toRatingDto(Rating rating);

 RatingResponse toRatingResponse(BaseApiResponse response);

 GetAllReviewResponse toGetAllReviewResponse(BaseApiResponse response);

 @Mapping(ignore = true,target = "rating")
 ReviewDto toReviewDto(Review review);

}
