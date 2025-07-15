package com.example.DocumentManagementSystem.Shared.DataTransferModel.User;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import lombok.Data;
import lombok.Getter;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.UUID;

@Data
@Getter
public class UserDto {

    private UUID userId;

    private String name;



    private String userName;


    private String mobileNumber;
    private String nationalID;

    private String email;



    private Role role;
    List<ObjectId> workSpaceList ;







}
