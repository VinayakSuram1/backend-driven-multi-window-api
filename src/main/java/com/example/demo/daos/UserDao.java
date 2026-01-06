package com.example.demo.daos;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.example.demo.entities.UserEn;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;

@Service
public class UserDao implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEn register(UserEn user) {
        // encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(null);

        return userRepository.save(user);
    }

    @Override
    public Optional<UserEn> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
