package com.example.DocumentManagementSystem.Shared.helperClasses.workspace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.WorkSpaceRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.ResourceNotFoundException;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.workspace.IWorkSpaceValidator;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceValidator implements IWorkSpaceValidator {
    private final WorkSpaceRepository workSpaceRepository;

    public WorkSpaceValidator(WorkSpaceRepository workSpaceRepository) {
        this.workSpaceRepository = workSpaceRepository;
    }
    @Override
    public WorkSpace validateExists(String id) {
        return workSpaceRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));    }
}
