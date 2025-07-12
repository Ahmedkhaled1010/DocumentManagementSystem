package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.WorkSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceService {

    @Autowired
    WorkSpaceRepository workSpaceRepository;
    public WorkSpace createWorkSpace(WorkSpace workSpace) {
        return workSpaceRepository.save(workSpace);
    }
}
