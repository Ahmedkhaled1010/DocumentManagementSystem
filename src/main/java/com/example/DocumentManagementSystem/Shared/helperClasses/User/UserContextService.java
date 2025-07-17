package com.example.DocumentManagementSystem.Shared.helperClasses.User;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.Exception.Exceptions.UserNotFoundException;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.user.IUserContextService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserContextService implements IUserContextService {
    @Override
    public User getCurrentUser(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User user) {
            return user;
        }
        throw new UserNotFoundException("Unauthenticated request");

    }
}
