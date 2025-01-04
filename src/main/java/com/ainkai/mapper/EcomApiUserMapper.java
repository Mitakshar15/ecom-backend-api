package com.ainkai.mapper;

import com.ainkai.api.BaseApiResponse;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.model.dtos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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

}
