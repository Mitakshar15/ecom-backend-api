package com.ainkai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainkai.model.User;

public interface UserRepo extends  JpaRepository<User,Long>{

    public User findByEmail(String email);
    

}
