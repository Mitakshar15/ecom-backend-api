package com.ainkai.controller;


import com.ainkai.model.User;
import com.ainkai.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminUserController {

    @Autowired
    private UserService userService;


    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsersHandler(@RequestHeader("Authorization") String jwt){

        List userList = userService.findAllUsers();

        return new ResponseEntity<>(userList, HttpStatus.OK);

    }
}
