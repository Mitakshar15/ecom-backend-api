package com.ainkai.service;

import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.model.dtos.AddressRequest;
import com.ainkai.model.dtos.EditProfileRequest;
import com.ainkai.request.EditUserRequest;
import com.ainkai.response.ApiResponse;

import java.util.List;


public interface UserService {

  public User findUserById(Long userId) throws UserException;

  public User findUserProfileByJwt(String jwt) ;

  public List<User> findAllUsers();

  public User editUser(EditProfileRequest user);

  public Address addNewAddress(com.ainkai.model.dtos.AddressRequest addressRequest);

  public void deleteAddress(Long addressId);

  public Address editAddress(AddressRequest addressRequest, Long addressId);

  public List<Address> getAllUserAddresses(Long userId);
}
