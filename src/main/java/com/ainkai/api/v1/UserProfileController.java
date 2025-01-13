/*
 * Copyright (c) 2025. Mitakshar.
 * All rights reserved.
 *
 * This is an e-commerce project built for Learning Purpose and may not be reproduced, distributed, or used without explicit permission from Mitakshar.
 *
 *
 */

package com.ainkai.api.v1;

import com.ainkai.api.EcomApiV1UserControllerApi;
import com.ainkai.builder.ApiResponseBuilder;
import com.ainkai.config.JwtProvider;
import com.ainkai.mapper.EcomApiUserMapper;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.model.dtos.*;
import com.ainkai.repository.AddressRepo;
import com.ainkai.repository.UserRepo;
import com.ainkai.response.ApiResponse;
import com.ainkai.service.CartService;
import com.ainkai.service.CustomUserServiceImpl;
import com.ainkai.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@RestController
@AllArgsConstructor
public class UserProfileController implements EcomApiV1UserControllerApi {

    private final UserRepo userRepo;
    private final UserService userService;
    private final CustomUserServiceImpl customUserService;
    private final JwtProvider jwtProvider;
    private final EcomApiUserMapper mapper;
    private final ApiResponseBuilder builder;
    private final CartService cartService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<SignUpResponse> signUp(@Valid SignUpRequest signUpRequest){

        User user = userRepo.findByEmail(signUpRequest.getEmail());
        if(user !=null){
           SignUpResponse response = mapper.toSignUpResponse(builder.buildErrorApiResponse("EMAIL ALREDY EXISTS"));
           return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        User createdUser = mapper.toUserEntity(signUpRequest);
        createdUser.setCreatedAt(LocalDateTime.now());
        createdUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        User savedUser = userRepo.save(createdUser);
        cartService.createCart(savedUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        SignUpResponse response = mapper.toSignUpResponse(builder.buildSuccessApiResponse("SIGNUP SUCCESS"));
        response.data(builder.buildAuthResponseDto(authentication));

        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    public ResponseEntity<SignInResponse> signIn(@Valid SignInRequest signInRequest){
        String userName = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        Authentication authentication = customUserService.authenticate(userName,password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        AuthResponseDto authResponseDto = new AuthResponseDto();
        authResponseDto.setMessage("Success");
        authResponseDto.setJwt(token);
        SignInResponse signInResponse = mapper.toSignInResponse(builder.buildSuccessApiResponse("SignIn Success"));
        signInResponse.data(authResponseDto);
        return  new ResponseEntity<>(signInResponse,HttpStatus.OK);

    }


    public ResponseEntity<GetProfileResponse> getUserProfileHandler(@RequestHeader("Authorization")String jwt)  {
        User user = null;
        user = userService.findUserProfileByJwt(jwt);
        if(user!=null) {
            GetProfileResponse getProfileResponse = mapper.toGetProfileResponse(builder.buildSuccessApiResponse("Get Profile Success"));
            getProfileResponse.data(mapper.toUserResponseDto(user));
            return new ResponseEntity<>(getProfileResponse, HttpStatus.OK);
        }
        else {
            GetProfileResponse getProfileErrorResponse = mapper.toGetProfileResponse(builder.buildErrorApiResponse("Invalid JWT"));
            return new ResponseEntity<>(getProfileErrorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<EditProfileResponse> updateProfileHandler(@RequestHeader("Authorization")String jwt,EditProfileRequest editProfileRequest) {
         User loggedUser = userService.findUserProfileByJwt(jwt);
         if(Objects.equals(loggedUser.getId(), editProfileRequest.getId())){
                 User updatedUser = userService.editUser(editProfileRequest);
                 EditProfileResponse editProfileResponse = mapper.toEditProfileResponse(builder.buildSuccessApiResponse("Edit Profile Success"));
                 editProfileResponse.data(mapper.toUserResponseDto(updatedUser));
                 return new ResponseEntity<>(editProfileResponse,HttpStatus.OK);
         }
         else{
             EditProfileResponse editProfileResponse = mapper.toEditProfileResponse(builder.buildErrorApiResponse("User Not Found"));
             return new ResponseEntity<>(editProfileResponse,HttpStatus.NOT_FOUND);
         }
    }

    public ResponseEntity<AddNewAddressResponse> addAddressHandler(@RequestHeader("Authorization")String jwt,AddressRequest addressRequest)  {
        User user = userService.findUserProfileByJwt(jwt);
        if(Objects.equals(user.getId(), addressRequest.getUserId())){
            Address newAddress = userService.addNewAddress(addressRequest);
            AddNewAddressResponse response = mapper.toAddNewAddressResponse(builder.buildSuccessApiResponse("Succesfully added new address"));
            return new ResponseEntity<>(response,HttpStatus.CREATED);
        }
        else {
            AddNewAddressResponse errorResponse = mapper.toAddNewAddressResponse(builder.buildErrorApiResponse("User Not Found, Please Login And try Again"));
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }

    }

    public ResponseEntity<DeleteAddressResponse> deleteAddressHandler(@PathVariable("addressId") Long addressId,@RequestHeader("Authorization")String jwt)  {
        User user = userService.findUserProfileByJwt(jwt);
        if(user !=null){
            userService.deleteAddress(addressId);
            DeleteAddressResponse deleteAddressResponse = mapper.toDeleteAddressResponse(builder.buildSuccessApiResponse("Succesfully deleted address"));
            return new ResponseEntity<>(deleteAddressResponse,HttpStatus.ACCEPTED);
        }
        else {
            DeleteAddressResponse errorResponse = mapper.toDeleteAddressResponse(builder.buildErrorApiResponse("Session Expired,Please Login And try Again"));
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }
    }


    public ResponseEntity<EditAddressResponse> editAddressHandler(@PathVariable("addressId")Long addressId, @NotNull @RequestHeader("Authorization")String jwt, @Valid AddressRequest addressRequest)  {
        User user = userService.findUserProfileByJwt(jwt);
        if(Objects.equals(user.getId(),addressRequest.getUserId())){
            userService.editAddress(addressRequest,addressId);
            EditAddressResponse editAddressResponse = mapper.toEditAddressResponse(builder.buildSuccessApiResponse("Succesfully edited address"));
            return new ResponseEntity<>(editAddressResponse,HttpStatus.ACCEPTED);
        }
        else {
            EditAddressResponse editAddressErrorResponse = mapper.toEditAddressResponse(builder.buildErrorApiResponse("Session Expired, Please Login And try Again"));
            return new ResponseEntity<>(editAddressErrorResponse,HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<GetAllAddressResponse> getAllAddressHandler(@RequestHeader("Authorization")String jwt)  {
        User user = userService.findUserProfileByJwt(jwt);
        if(user !=null){
            List<Address> addressList = userService.getAllUserAddresses(user.getId());
            GetAllAddressResponse response = mapper.toGetAllAddressResponse(builder.buildSuccessApiResponse("Get All Addresses Success"));
            response.data(mapper.toAddressDtoList(addressList));
            return new ResponseEntity<>(response,HttpStatus.OK);
        }
        else {
            GetAllAddressResponse response = mapper.toGetAllAddressResponse(builder.buildErrorApiResponse("Session Expired,Please Login And try Again"));
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }


}
