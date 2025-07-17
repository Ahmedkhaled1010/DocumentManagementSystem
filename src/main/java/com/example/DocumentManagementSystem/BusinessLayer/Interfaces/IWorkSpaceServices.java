package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;

public interface IWorkSpaceServices {
     WorkSpaceDto createWorkSpace(WorkSpaceDto workSpaceDto, User user);
     void createFirstWorkSpace( User user);
     WorkSpaceDto deleteWorkSpace(String workSpaceId,Boolean isDeleted);
      WorkSpaceDto retriveWorkSpace(String workSpaceId, User user);
    WorkSpaceDto updateWorkSpace(String workSpaceId,  WorkSpaceDto workSpaceDto, User user);
    Page<WorkSpaceDto> retriveAllWorkSpace(Authentication authentication, int pageNum, int pageSize, String sortField, String sortDir);
}
