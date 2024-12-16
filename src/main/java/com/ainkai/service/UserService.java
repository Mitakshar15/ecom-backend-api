package com.ainkai.service;

import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.request.AddressRequest;
import com.ainkai.response.ApiResponse;

import java.util.List;


public interface UserService {

  public User findUserById(Long userId) throws UserException;

  public User findUserProfileByJwt(String jwt) throws UserException;

  public List<User> findAllUsers();






}
