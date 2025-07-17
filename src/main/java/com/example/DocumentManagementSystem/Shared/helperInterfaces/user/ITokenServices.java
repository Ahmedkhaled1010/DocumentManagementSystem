package com.example.DocumentManagementSystem.Shared.helperInterfaces.user;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;

public interface ITokenServices {
    public String generateJwtToken(User user);
}
