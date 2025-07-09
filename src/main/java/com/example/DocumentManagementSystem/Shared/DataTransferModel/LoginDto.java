package com.example.DocumentManagementSystem.Shared.DataTransferModel;

import com.example.DocumentManagementSystem.Shared.annotation.PasswordValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {
    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
    private String email;
    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")

    private String password;
}
