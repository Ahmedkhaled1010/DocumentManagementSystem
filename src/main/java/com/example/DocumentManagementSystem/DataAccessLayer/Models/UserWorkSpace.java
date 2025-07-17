package com.example.DocumentManagementSystem.DataAccessLayer.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "user_workspaces")
public class UserWorkSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "workspace_mongo_id")
    private String workspaceMongoId;
}
