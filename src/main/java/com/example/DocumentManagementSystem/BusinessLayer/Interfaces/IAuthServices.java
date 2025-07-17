package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.UserDto;
import org.springframework.security.core.Authentication;

public interface IAuthServices {

    public String createUser(RegisterDto registerDto);
    public String login(RegisterDto registerDto);
    public UserDto getUserDetails(Authentication authentication);
}
