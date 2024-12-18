package com.ainkai.service;

import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.request.AddressRequest;
import com.ainkai.request.EditUserRequest;
import com.ainkai.response.ApiResponse;

import java.util.List;


public interface UserService {

  public User findUserById(Long userId) throws UserException;

  public User findUserProfileByJwt(String jwt) throws UserException;

  public List<User> findAllUsers();

  public User editUser(EditUserRequest user);

  public Address addNewAddress(AddressRequest addressRequest);

  public void deleteAddress(Long addressId);
}
