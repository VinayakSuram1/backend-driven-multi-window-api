package com.example.demo.services;

import java.util.Optional;

import com.example.demo.entities.UserEn;

public interface UserService {

    UserEn register(UserEn user);
    Optional<UserEn> findByEmail(String email);
  
}
