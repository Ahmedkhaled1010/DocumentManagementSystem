package com.example.DocumentManagementSystem.Shared.helperInterfaces.workspace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;

public interface IWorkSpaceValidator {
    WorkSpace validateExists(String id);
}
