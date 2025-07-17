package com.example.DocumentManagementSystem.PresentationLayer.Controller;

import com.example.DocumentManagementSystem.BusinessLayer.Services.AuthServices;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.LoginDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.RegisterDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.User.UserDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

public class PublicController {

    @Autowired
   private final AuthServices authServices;
    PublicController(AuthServices authServices) {
        this.authServices = authServices;
    }

    @RequestMapping("/register")
    public ResponseEntity<APIResponse<Map<String, String>>> createUser(@Valid @RequestBody RegisterDto user ) {

            String token = authServices.createUser(user);
        APIResponse<Map<String, String>>   response = new APIResponse<>(HttpStatus.CREATED.name(), "Registration successful",  Map.of("token", token));

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @RequestMapping("/login")
    public ResponseEntity<APIResponse<Map<String, String>>> login(@RequestBody LoginDto login) {

        String token=authServices.login(login);
        APIResponse<Map<String, String>>   response = new APIResponse<>(HttpStatus.CREATED.name(), "Login successful",  Map.of("token", token));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @RequestMapping("/me")
    public ResponseEntity<APIResponse<UserDto>> getUserDetails( Authentication authentication) {
        UserDto userDto = authServices.getUserDetails(authentication);
        APIResponse<UserDto>   response = new APIResponse<>(HttpStatus.CREATED.name(), "getUserDetails",  userDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
