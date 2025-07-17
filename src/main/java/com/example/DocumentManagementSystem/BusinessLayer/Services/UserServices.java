package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.BusinessLayer.Interfaces.IUserServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.UnauthorizedAccessException;
import com.example.DocumentManagementSystem.Exception.Exceptions.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices implements IUserServices {
    @Autowired
    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username).orElseThrow(() -> new UserNotFoundException("User not found"));
    }


}
