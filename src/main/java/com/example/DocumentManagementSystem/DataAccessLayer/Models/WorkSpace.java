package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import jakarta.persistence.Entity;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
@Document(collection = "workspaces")
public class WorkSpace extends BaseEntity {

    @Id
    @Field("_id")

    private ObjectId id;
    private String name;
    private String description;

    private String userNationalID;
    private Boolean isDeleted=false;

    @DBRef
    private List<Documnet> documents;


}
