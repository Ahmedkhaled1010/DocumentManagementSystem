package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "documnets")
public class Documnet extends BaseEntity {

    @Id
    private String id;
    private String workspaceId;
    private String title;
    private String type;
    private Boolean isDeleted;
    private String content;
}
