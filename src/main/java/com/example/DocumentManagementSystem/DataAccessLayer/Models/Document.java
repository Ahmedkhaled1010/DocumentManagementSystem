package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import com.example.DocumentManagementSystem.Shared.Enum.Privacy;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document extends BaseEntity {

    @Id
    private ObjectId id;
    private ObjectId workspaceId;
    private String fileName;
    private String fileType;
    private String filePath;
    private String nationalID;

    private long size;
    private Boolean isDeleted=false;
    private String tag = "GENERAL";
    private String privacy= Privacy.PRIVATE.toString();
}
