package com.ainkai.service;

import com.ainkai.config.JwtProvider;
import com.ainkai.exceptions.UserException;
import com.ainkai.model.User;
import com.ainkai.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class userServiceImpl implements UserService{

    private UserRepo userRepo;
    private JwtProvider jwtProvider;

    public  userServiceImpl( UserRepo userRepo, JwtProvider jwtProvider){
      this.userRepo = userRepo;
      this.jwtProvider = jwtProvider;
    }


    @Override
    public User findUserById(Long userId) throws UserException {

        Optional<User>user = userRepo.findById(userId);
        if(user.isPresent()){
            return user.get();
        }


        throw new UserException(":: USER NOT FOUND WITH ID ::"+ userId);
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        User user  =userRepo.findByEmail(email);

        if(user ==null){
            throw new UserException(":: USER NOT FOUND FOR ::"+ email);
        }


        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }
}
