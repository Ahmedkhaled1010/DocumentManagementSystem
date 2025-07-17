package com.example.DocumentManagementSystem.Shared.helperInterfaces.user;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import org.springframework.security.core.Authentication;

public interface IUserContextService {
    public User getCurrentUser(Authentication authentication);
}
