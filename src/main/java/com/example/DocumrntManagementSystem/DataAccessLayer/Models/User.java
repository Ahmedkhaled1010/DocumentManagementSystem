package com.example.DocumrntManagementSystem.DataAccessLayer.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Getter
@Table(name = "Users")
public class User extends BaseEntity {

        @Id
        @GeneratedValue(strategy= GenerationType.AUTO,generator="native")
        @GenericGenerator(name = "native",strategy = "native")
        private int userId;
    @NotBlank(message=" Name must not be blank")
    @Size(min=5, message="Name must be at least 5 characters long")
    private String name;

    @NotBlank(message="User Name must not be blank")
    @Size(min=3, message="User Name must be at least 3 characters long")
        private String userName;
    @NotBlank(message="Mobile number must not be blank")
    @Pattern(regexp="(^$|^01[0-9]{9}$)",message = "Mobile number must be 11 digits And Satrt 01")

        private String mobileNumber;
    @NotBlank(message="Email must not be blank")
    @Email(message = "Please provide a valid email address" )
        private String email;
    @NotBlank(message="Confirm Email must not be blank")
    @Email(message = "Please provide a valid confirm email address" )
    @Transient
    @JsonIgnore
        private String confirmEmail;
    @NotBlank(message="Password must not be blank")
    @Size(min=5, message="Password must be at least 5 characters long")
        private String password;
    @NotBlank(message="Confirm Password must not be blank")
    @Size(min=5, message="Confirm Password must be at least 5 characters long")
    @Transient
    @JsonIgnore
        private String confirmPassword;
    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST, targetEntity = Role.class)
    @JoinColumn(name = "role_id", referencedColumnName = "roleId",nullable = false)
    private Role role;


}
