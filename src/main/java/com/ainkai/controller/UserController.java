package com.ainkai.controller;


import com.ainkai.exceptions.UserException;
import com.ainkai.model.User;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
