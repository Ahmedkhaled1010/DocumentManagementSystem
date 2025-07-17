package com.example.DocumentManagementSystem.Shared.helperInterfaces.workspace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;

public interface IWorkSpaceDeletionService {
    void softDeleteWorkSpace(WorkSpace workspace);
}
