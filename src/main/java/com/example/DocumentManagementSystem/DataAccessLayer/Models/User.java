package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import com.example.DocumentManagementSystem.Shared.annotation.FieldsValueMatch;
import com.example.DocumentManagementSystem.Shared.annotation.PasswordValidator;
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

@Entity
@Data
@Getter
@Table(name = "Users")
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
public class User extends BaseEntity {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "user_id", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
    private UUID userId;

    private String name;



    private String userName;


    private String mobileNumber;
    private String nationalID;

    private String email;

    @Transient
    @JsonIgnore

    private String confirmEmail;
    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    private String confirmPassword;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;



}
