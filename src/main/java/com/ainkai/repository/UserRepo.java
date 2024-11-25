package com.ainkai.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ainkai.model.User;

import java.util.List;

public interface UserRepo extends  JpaRepository<User,Long>{

    public User findByEmail(String email);

    public List<User> findAllByOrderByCreatedAtDesc();
}
