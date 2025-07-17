package com.example.DocumentManagementSystem.Shared.helperClasses.User;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.InvalidCredentialsException;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.user.IUserAuthenticator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class UserAuthenticator implements IUserAuthenticator {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAuthenticator(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public User authenticate(String email, String rawPassword) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent() && passwordEncoder.matches(rawPassword, user.get().getPassword())) {
            return user.get();
        }

        throw new InvalidCredentialsException("Invalid credentials");
    }
}
