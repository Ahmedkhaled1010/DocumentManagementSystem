package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.BusinessLayer.Interfaces.IWorkSpaceServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.UserWorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserWorkSpaceRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.DocumentRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.WorkSpaceRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.ResourceNotFoundException;
import com.example.DocumentManagementSystem.Exception.Exceptions.UnauthorizedAccessException;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import com.example.DocumentManagementSystem.Shared.POJO.ApiResponsePage;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserContextService;
import com.example.DocumentManagementSystem.Shared.helperClasses.workspace.WorkSpaceDeletionService;
import com.example.DocumentManagementSystem.Shared.helperClasses.workspace.WorkSpaceValidator;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;

@Service
public class WorkSpaceService implements IWorkSpaceServices {

    private final WorkSpaceRepository workSpaceRepository;

    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final UserWorkspaceServices userWorkspaceServices;
    private final WorkSpaceDeletionService workSpaceDeletionService;
    private final WorkSpaceValidator workSpaceValidator;
    private final UserContextService userContextService;
    private final ModelMapper modelMapper;

    @Autowired
    public WorkSpaceService(WorkSpaceRepository workSpaceRepository,
                            DocumentRepository documentRepository,
                            UserRepository userRepository,
                            UserWorkspaceServices userWorkspaceServices,
                            WorkSpaceDeletionService workSpaceDeletionService,
                            WorkSpaceValidator workSpaceValidator,
                            UserContextService userContextService,
                            ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.workSpaceRepository = workSpaceRepository;
        this.documentRepository = documentRepository;
        this.userRepository = userRepository;
        this.userWorkspaceServices = userWorkspaceServices;
        this.workSpaceDeletionService = workSpaceDeletionService;
        this.workSpaceValidator = workSpaceValidator;
        this.userContextService = userContextService;
    }


    @Autowired
    UserServices userServices;

    @Autowired
    private MongoTemplate mongoTemplate;

    public WorkSpaceDto createWorkSpace(WorkSpaceDto workSpaceDto, User user) {
        {

            WorkSpace workSpace = modelMapper.map(workSpaceDto, WorkSpace.class);
            workSpace.setUserNationalID(user.getNationalID());
            WorkSpace WorkSpaceSaved = workSpaceRepository.save(workSpace);
            userWorkspaceServices.addWorkspace(user.getUserId(), WorkSpaceSaved.getId().toString());
            WorkSpaceDto workSpaceDto1 = modelMapper.map(WorkSpaceSaved, WorkSpaceDto.class);

            return workSpaceDto1;


        }
    }

    //Create First WorkSpace When User Register
    public void createFirstWorkSpace(User user) {
        {

            WorkSpaceDto workSpace = new WorkSpaceDto();
            workSpace.setDescription("Hello " + user.getUserName() + "  First WorkSpace");
            workSpace.setName(user.getUserName() + "  Work Space");
            createWorkSpace(workSpace, user);


        }
    }


    public WorkSpaceDto deleteWorkSpace(String workSpaceId, Boolean isDeleted) {
        WorkSpace workSpace = workSpaceValidator.validateExists(workSpaceId);
        workSpaceDeletionService.softDeleteWorkSpace(workSpace);
        workSpace = workSpaceValidator.validateExists(workSpaceId);
        WorkSpaceDto workSpaceDto = modelMapper.map(workSpace, WorkSpaceDto.class);
        return workSpaceDto;

    }

    public WorkSpaceDto retriveWorkSpace(String workSpaceId, User user) {

        WorkSpace workSpace = findWorkSpaceById(workSpaceId);
        if (!isUserAuthorizedForWorkspace(user.getUserId(), workSpaceId)) {
            throw new UnauthorizedAccessException("User is not authorized to access this workspace.");
        }
        WorkSpaceDto workSpaceDto = modelMapper.map(workSpace, WorkSpaceDto.class);
        return workSpaceDto;


    }

    public Page<WorkSpaceDto> retriveAllWorkSpace(Authentication authentication, int pageNum, int pageSize, String sortField, String sortDir) {

        Pageable pageable = buildPageRequest(pageNum, pageSize, sortField, sortDir);
        User user = userContextService.getCurrentUser(authentication);
        Page<WorkSpace> userWorkSpace = workSpaceRepository.findByUserNationalID(user.getNationalID(), pageable);

        List<WorkSpaceDto> workSpaceDtoList = convertToDtoList(userWorkSpace.getContent());
        Page<WorkSpaceDto> workSpaceDtos = new PageImpl<>(workSpaceDtoList, pageable, userWorkSpace.getTotalElements());


        return workSpaceDtos;
    }


    public WorkSpaceDto updateWorkSpace(String workSpaceId, WorkSpaceDto workSpaceDto, User user) {

        if (!isUserAuthorizedForWorkspace(user.getUserId(), workSpaceId)) {
            throw new UnauthorizedAccessException("User is not authorized to access this workspace.");
        }

        WorkSpace workSpace = findWorkSpaceById(workSpaceId);

        updateWorkSpaceFromDto(workSpace, workSpaceDto);

        workSpace = workSpaceRepository.save(workSpace);

        return modelMapper.map(workSpace, WorkSpaceDto.class);

    }


    public Page<WorkSpaceDto> retriveAllWorkSpaceAllUser(User user, int pageNum, int pageSize, String sortField, String sortDir) {
        Pageable pageable = buildPageRequest(pageNum, pageSize, sortField, sortDir);
        Page<WorkSpace> workSpaceList = workSpaceRepository.findAll(pageable);

        if (workSpaceList.isEmpty()) {
            throw new ResourceNotFoundException("WorkSpace not found");
        }
        List<WorkSpaceDto> workSpaceDtoList = convertToDtoList(workSpaceList.getContent());
        Page<WorkSpaceDto> workSpaceDtos = new PageImpl<>(workSpaceDtoList, pageable, workSpaceList.getTotalElements());


        return workSpaceDtos;

    }


    private WorkSpace findWorkSpaceById(String workSpaceId) {
        return workSpaceRepository.findById(new ObjectId(workSpaceId))
                .orElseThrow(() -> new ResourceNotFoundException("WorkSpace not found"));
    }

    private boolean isUserAuthorizedForWorkspace(UUID userId, String workspaceId) {
        List<UserWorkSpace> userList = userWorkspaceServices.getUserWorkSpace(userId);
        return userList.stream().anyMatch(uw -> uw.getWorkspaceMongoId().equals(workspaceId));
    }

    private void updateWorkSpaceFromDto(WorkSpace workSpace, WorkSpaceDto dto) {
        if (dto.getName() != null) {
            workSpace.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            workSpace.setDescription(dto.getDescription());
        }
    }
    private Pageable buildPageRequest(int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return PageRequest.of(pageNum - 1, pageSize, sort);
    }

    private List<WorkSpaceDto> convertToDtoList(List<WorkSpace> workSpaces) {
        return workSpaces.stream()
                .map(ws -> modelMapper.map(ws, WorkSpaceDto.class))
                .toList();
    }

}
