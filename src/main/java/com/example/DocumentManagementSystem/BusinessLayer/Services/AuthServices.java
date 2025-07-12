package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.RolesRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.UserRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.JWT.JwtUtil;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.LoginDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.RegisterDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.UserDto;
import com.example.DocumentManagementSystem.Shared.Enum.Roles;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AuthServices {

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
    public ResponseEntity<APIResponse<Map<String, String>>> createUser(@Valid @RequestBody RegisterDto registerDto, BindingResult bindingResult) {


        APIResponse<Map<String, String>> response ;

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });

            bindingResult.getGlobalErrors().forEach(error -> {
                errors.put("global", error.getDefaultMessage());
            });


            response = new APIResponse<>("400", "Error", errors);
            return ResponseEntity.badRequest().body(response);
        }
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            response = new APIResponse<>("400", "User already exists", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userRepository.findByUserName(registerDto.getUserName()).isPresent()) {
            response = new APIResponse<>("400", "Username Already taken", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (userRepository.findByNationalID(registerDto.getNationalID()).isPresent()) {
            response = new APIResponse<>("400", "NationalID Already taken", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        Role role = rolesRepository.getByRoleName(Roles.USER.name());
        log.info(role.getRoleName());
        User user =modelMapper.map(registerDto,User.class);
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        User result = userRepository.save(user);

        if (result != null && result.getUserId() !=null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", result.getUserId());
            claims.put("role", result.getRole().getRoleName());

            String token = jwtUtil.generateToken(claims, result.getUserName());
            response = new APIResponse<>(HttpStatus.CREATED.name(), "Registration successful",  Map.of("token", token));
            return ResponseEntity.ok(response);
        }
        response = new APIResponse<>("401", "Registration Failed ", null);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<APIResponse<Map<String, String>> > login(@RequestBody LoginDto login)
    {
        Optional<User> user = userRepository.findByEmail(login.getEmail());



        if (user.isPresent()&& passwordEncoder.matches(login.getPassword(), user.get().getPassword())){

            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", user.get().getUserId());
            claims.put("role", user.get().getRole().getRoleName());

            String token = jwtUtil.generateToken(claims,user.get().getUserName());
            APIResponse<Map<String, String>> response = new APIResponse<>(HttpStatus.CREATED.name(), "Login successful", Map.of("token", token));

            return ResponseEntity.ok(response);
        }


        else
        {
            APIResponse<Map<String, String>> response = new APIResponse<>("401", "Invalid credentials", null);
            return ResponseEntity.status(401).body(response);
        }

    }

    public ResponseEntity<APIResponse< UserDto>> getUserDetails(Authentication authentication) {


        User user =(User) authentication.getPrincipal();
        if (user!=null) {
            Optional<User> user1 = userRepository.findByUserName(user.getUserName());
            if (user1.isPresent()) {
                UserDto userDto = modelMapper.map(user1.get(), UserDto.class);

                return ResponseEntity.ok(new APIResponse<>("200", "User details", userDto));
            }
        }
        return ResponseEntity.ok(new APIResponse<>("401", "User not found", null));
    }
}
