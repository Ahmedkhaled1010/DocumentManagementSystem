package com.example.DocumentManagementSystem.Shared.DataTransferModel;

import com.example.DocumentManagementSystem.Shared.annotation.FieldsValueMatch;
import com.example.DocumentManagementSystem.Shared.annotation.PasswordValidator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        ),
        @FieldsValueMatch(
                field = "email",
                fieldMatch = "confirmEmail",
                message = "Email addresses do not match!"
        )
})
public class RegisterDto {

    @NotBlank(message = "Name must not be blank")
    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    @NotBlank(message = "User Name must not be blank")
    @Size(min = 3, message = "User Name must be at least 3 characters long")
    private String userName;

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp = "^01[0-9]{9}$", message = "Mobile number must be 11 digits and start with 01")
    private String mobileNumber;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Confirm Email must not be blank")
    @Email(message = "Please provide a valid confirm email address")
    private String confirmEmail;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
    @PasswordValidator
    private String password;

    @NotBlank(message = "Confirm Password must not be blank")
    @Size(min = 5, message = "Confirm Password must be at least 5 characters long")
    private String confirmPassword;
}
