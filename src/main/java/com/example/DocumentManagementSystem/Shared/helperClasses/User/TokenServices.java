package com.example.DocumentManagementSystem.Shared.helperClasses.User;

import com.example.DocumentManagementSystem.DataAccessLayer.JWT.JwtUtil;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.Exception.Exceptions.RegistrationFailedException;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.user.ITokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class TokenServices implements ITokenServices {
    private final JwtUtil jwtUtil;
    @Autowired
    public TokenServices(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    public String generateJwtToken(User user) {
        if (user != null && user.getUserId() != null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.getUserId());
            claims.put("role", user.getRole().getRoleName());
            return jwtUtil.generateToken(claims, user.getUserName());
        }
        throw new RegistrationFailedException("Registration failed. Please check your input.");
    }

}
