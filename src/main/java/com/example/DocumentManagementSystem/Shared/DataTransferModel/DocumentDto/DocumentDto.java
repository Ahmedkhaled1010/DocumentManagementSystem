package com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto;

import com.example.DocumentManagementSystem.Shared.Enum.Privacy;
import lombok.Data;
import org.bson.types.ObjectId;
@Data
public class    DocumentDto {
    private ObjectId id;
    private ObjectId workspaceId;
    private String fileName;
    private String fileType;
    private String filePath;
    private String nationalID;

    private long size;
    private String title;
    private Boolean isDeleted=false;
    private String privacy= Privacy.PRIVATE.toString();
}
