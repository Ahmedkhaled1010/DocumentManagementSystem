package com.example.DocumentManagementSystem.Shared.helperInterfaces.user;

import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;

public interface IUserValidator {
    public void validateNewUser(RegisterDto registerDto);
}
