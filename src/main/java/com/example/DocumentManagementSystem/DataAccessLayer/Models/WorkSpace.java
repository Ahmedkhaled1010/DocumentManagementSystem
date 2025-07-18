package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;


@Data
@org.springframework.data.mongodb.core.mapping.Document(collection = "workspaces")
public class WorkSpace extends BaseEntity {

    @Id
    @Field("_id")

    private ObjectId id;
    private String name;
    private String description;

    private String userNationalID;
    private Boolean isDeleted=false;

    @DBRef
    private List<Document> documents;


}
