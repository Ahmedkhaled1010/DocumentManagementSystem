package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import org.springframework.security.core.Authentication;

public interface IUserServices {

    User findByUserName(String username);
}
