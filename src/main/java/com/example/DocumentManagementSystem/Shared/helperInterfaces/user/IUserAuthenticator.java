package com.example.DocumentManagementSystem.Shared.helperInterfaces.user;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;

public interface IUserAuthenticator {
    public User authenticate(String email, String rawPassword);
}
