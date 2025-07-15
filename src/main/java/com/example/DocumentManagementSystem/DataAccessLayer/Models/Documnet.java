package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Data
@Document(collection = "documnets")
public class Documnet extends BaseEntity {

    @Id
    private String id;
    private String workspaceId;
    private String fileName;
    private String fileType;
    private String filePath;

    private long size;
    private String title;
    private Boolean isDeleted;
}
