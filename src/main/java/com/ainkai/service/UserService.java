package com.ainkai.service;

import com.ainkai.exceptions.UserException;
import com.ainkai.model.User;

import java.util.List;


public interface UserService {

  public User findUserById(Long userId) throws UserException;

  public User findUserProfileByJwt(String jwt) throws UserException;

  public List<User> findAllUsers();

}
