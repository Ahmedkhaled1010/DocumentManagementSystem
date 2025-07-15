package com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @NotBlank(message = "National ID must not be blank")
    @Pattern(regexp = "^[0-9]{14}$", message = "National ID must be 14 digits")
    private String userNationalID;
    private Boolean isDeleted=false;

    private List<Documnet> documents;
}
