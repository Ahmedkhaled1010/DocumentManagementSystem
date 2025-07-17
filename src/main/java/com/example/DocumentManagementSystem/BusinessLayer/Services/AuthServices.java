package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.RolesRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.JWT.JwtUtil;
import com.example.DocumentManagementSystem.Exception.Exceptions.InvalidCredentialsException;
import com.example.DocumentManagementSystem.Exception.Exceptions.UserNotFoundException;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.LoginDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.UserDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.TokenServices;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserAuthenticator;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserContextService;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthServices {
    private final WorkSpaceService workSpaceService;
    private final RoleServices roleServices;
    private final UserValidator userValidator;
    private final TokenServices tokenServices ;
    private final UserAuthenticator userAuthenticator;
    private final UserContextService userContextService;
    private final  UserServices userServices;
    @Autowired

    public AuthServices(WorkSpaceService workSpaceService,
                        RoleServices roleServices,
                        UserValidator userValidator,
                        TokenServices tokenServices,
                        UserAuthenticator userAuthenticator,
                        UserContextService userContextService,
                        UserServices userServices) {
        this.workSpaceService = workSpaceService;
        this.roleServices = roleServices;
        this.userValidator = userValidator;
        this.tokenServices = tokenServices;
        this.userAuthenticator = userAuthenticator;
        this.userContextService = userContextService;
        this.userServices = userServices;

    }
    @Autowired
    UserRepository userRepository;
    @Autowired
    RolesRepository rolesRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;

    public String createUser( RegisterDto registerDto) {

        userValidator.validateNewUser(registerDto);
        User user =modelMapper.map(registerDto,User.class);
        user = roleServices.assignRole(user);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        User result = userRepository.save(user);
        workSpaceService.createFirstWorkSpace(result);


       String token = tokenServices.generateJwtToken(result);
        return token;
    }

    public String login( LoginDto login) {
        User user = userAuthenticator.authenticate(login.getEmail(), login.getPassword());
        String token = tokenServices.generateJwtToken(user);

        return token;
    }

    public UserDto getUserDetails(Authentication authentication) {



        User currentuser =userContextService.getCurrentUser(authentication);
        User user = userServices.findByUserName(currentuser.getUserName());
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }
}
