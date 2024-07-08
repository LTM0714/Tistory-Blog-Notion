package com.example.myfirstproject.service;

import com.example.myfirstproject.dto.JoinRequest;
import com.example.myfirstproject.dto.LoginRequest;
import com.example.myfirstproject.entity.User;
import com.example.myfirstproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User getLoginUserById(Long userId) {
        if(userId == null) return null;
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isEmpty()) return null;
        return optionalUser.get();
    }

    @Transactional
    public void join(JoinRequest req) {
        userRepository.save(req.toEntity());
    }

    public User login(LoginRequest req) {
        Optional<User> optionalUser = userRepository.findByLoginId(req.getLoginId());
        if(optionalUser.isEmpty()) {
            return null;
        }
        User user = optionalUser.get();
        if(!user.getPassword().equals(req.getPassword())) {
            return null;
        }
        return user;
    }
}