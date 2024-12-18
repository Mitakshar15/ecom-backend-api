package com.ainkai.controller;


import com.ainkai.exceptions.UserException;
import com.ainkai.model.Address;
import com.ainkai.model.User;
import com.ainkai.request.AddressRequest;
import com.ainkai.request.EditUserRequest;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt)throws UserException{

        System.out.println("INSIDE GET USER PROFILE HANDLER CONTROLLER ::");
        User user = userService.findUserProfileByJwt(jwt);
        return  new ResponseEntity<>(user, HttpStatus.ACCEPTED);

    }

    @GetMapping("/")
    public ResponseEntity<List<User>> findAllUsersHandler(@RequestHeader("Authorization") String jwt) throws UserException{

        List<User> users = userService.findAllUsers();
        return  new ResponseEntity<>(users,HttpStatus.ACCEPTED);

    }

    @PutMapping("/editProfile")
    public ResponseEntity<User>updateUserDetailHandler(@RequestHeader("Authorization") String jwt, @RequestBody EditUserRequest userRequest) throws UserException {

        User loggedUser = userService.findUserProfileByJwt(jwt);
        if(Objects.equals(loggedUser.getId(), userRequest.getUserId())){
            User updatedUser = userService.editUser(userRequest);
            return new ResponseEntity<User>(updatedUser,HttpStatus.OK);
        }
        else {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/address/edit")
    public ResponseEntity<Address>editDeliveryAddress(@RequestHeader("Authorization") String jwt,@RequestBody AddressRequest request) throws UserException {

       User user  = userService.findUserProfileByJwt(jwt);
       if(Objects.equals(user.getId(), request.getUserId())){
           Address newAddress = userService.addNewAddress(request);
           if(newAddress!=null){
               return new ResponseEntity<>(newAddress,HttpStatus.CREATED);
           }
           else {
               return ResponseEntity.notFound().build();
           }

       }
       else{
           return ResponseEntity.notFound().build();
       }

    }



}
