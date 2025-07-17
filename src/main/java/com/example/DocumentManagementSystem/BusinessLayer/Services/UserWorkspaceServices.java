package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.BusinessLayer.Interfaces.IUserWorkspace;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.UserWorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserWorkSpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserWorkspaceServices implements IUserWorkspace {
    private final UserWorkSpaceRepository userWorkSpaceRepository;
    @Autowired
    public UserWorkspaceServices(UserWorkSpaceRepository userWorkSpaceRepository) {
        this.userWorkSpaceRepository = userWorkSpaceRepository;

    }
    @Override
    public void addWorkspace(UUID userId, String workSpaceId) {
        UserWorkSpace link = new UserWorkSpace();
        link.setUserId(userId);
        link.setWorkspaceMongoId(workSpaceId);
        userWorkSpaceRepository.save(link);
    }
    public List<UserWorkSpace> getUserWorkSpace(UUID userId){
        return userWorkSpaceRepository.findByUserId(userId);
    }
}
