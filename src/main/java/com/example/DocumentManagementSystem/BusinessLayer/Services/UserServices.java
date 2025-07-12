package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.RolesRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.UserRepository;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.UserDto;
import com.example.DocumentManagementSystem.Shared.Enum.Roles;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.tree.RowMapper;

@Slf4j
@Service
public class UserServices {
    @Autowired
    UserRepository userRepository;
public User findByUserName(String username)
{
    return userRepository.findByUserName(username).get();}

}
