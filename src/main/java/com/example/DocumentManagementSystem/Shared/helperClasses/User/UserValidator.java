package com.example.DocumentManagementSystem.Shared.helperClasses.User;

import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.user.IUserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.DocumentManagementSystem.Exception.Exceptions.EmailAlreadyExistsException;
import com.example.DocumentManagementSystem.Exception.Exceptions.NationalIDAlreadyExistsException;
import com.example.DocumentManagementSystem.Exception.Exceptions.UsernameAlreadyExistsException;
@Component
public class UserValidator implements IUserValidator {
    private final UserRepository userRepository;
    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public void validateNewUser(RegisterDto registerDto) {
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email Already taken");
        }
        if (userRepository.findByUserName(registerDto.getUserName()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username Already taken");
        }
        if (userRepository.findByNationalID(registerDto.getNationalID()).isPresent()) {
            throw new NationalIDAlreadyExistsException("National ID Already taken");
        }
    }
}
