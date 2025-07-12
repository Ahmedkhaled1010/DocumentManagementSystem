package com.example.DocumentManagementSystem.Shared.DataTransferModel;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@Getter
public class UserDto {


    private UUID userId;

    private String name;


    private String userName;

    private String mobileNumber;
    private String NationalID;


    private String email;



}
