package com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.List;
@Data
public class WorkSpaceDto {

    private ObjectId id;
    @NotBlank(message = "name must not be blank")

    private String name;
    @NotBlank(message = "description must not be blank")

    private String description;

    private Boolean isDeleted=false;

    private List<Document> documents;
}
