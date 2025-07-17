package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import java.util.UUID;

public interface IUserWorkspace {

    void addWorkspace(UUID userId, String workSpaceId);
}
