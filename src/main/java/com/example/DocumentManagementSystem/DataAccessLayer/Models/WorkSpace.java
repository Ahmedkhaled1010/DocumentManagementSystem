package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import jakarta.persistence.Entity;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "workspaces")
public class WorkSpace extends BaseEntity {
    @Id
    private String Id;
    private String name;
    private String description;

    private String userNationalID;


}
