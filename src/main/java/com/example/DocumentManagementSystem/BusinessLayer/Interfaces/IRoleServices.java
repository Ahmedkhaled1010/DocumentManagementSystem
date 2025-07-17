package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import org.springframework.security.core.Authentication;

public interface IRoleServices {

     User assignRole(User user);
    void checkRole(Authentication authentication);

}
