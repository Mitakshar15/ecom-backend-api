//This Class is responisble for Removing default auth and adding custom auth in spring security


package com.ainkai.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ainkai.model.User;
import com.ainkai.repository.UserRepo;

@Service
public class CustomUserServiceImpl implements UserDetailsService {

    private UserRepo userRepository;

    public CustomUserServiceImpl(UserRepo userRepository) {
        this.userRepository=userRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user == null) {
            throw new UsernameNotFoundException("user not found with email "+ username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),authorities);
    }
}
