package com.example.DocumentManagementSystem.PresentationLayer.Controller;

import com.example.DocumentManagementSystem.BusinessLayer.Services.AuthServices;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.LoginDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.UserDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;


import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:4200")

public class PublicController {

    @Autowired
    AuthServices authServices;


    @RequestMapping("/register")
    public ResponseEntity<APIResponse<Map<String, String>>> createUser(@Valid @RequestBody RegisterDto user , BindingResult bindingResult) {



            return authServices.createUser(user,bindingResult);
    }
    @RequestMapping("/login")
    public ResponseEntity<APIResponse<Map<String, String>>> login(@RequestBody LoginDto login) {


        return authServices.login(login);
    }
    @RequestMapping("/me")
    public ResponseEntity<APIResponse<UserDto>> getUserDetails( Authentication authentication) {
        return authServices.getUserDetails(authentication);
    }
}
