package com.example.DocumentManagementSystem.PresentationLayer.Controller;


import com.example.DocumentManagementSystem.BusinessLayer.Services.RoleServices;
import com.example.DocumentManagementSystem.BusinessLayer.Services.WorkSpaceService;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.UserWorkSpace;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import com.example.DocumentManagementSystem.Shared.POJO.ApiResponsePage;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserContextService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workspace")
@Slf4j

public class WorkSpaceController {
    @Autowired

   private final WorkSpaceService workSpaceService;
    private final UserContextService userContextService;
    private final RoleServices roleServices;
    public WorkSpaceController(WorkSpaceService workSpaceService,
                               UserContextService userContextService,
                               RoleServices roleServices) {
        this.roleServices = roleServices;
        this.userContextService = userContextService;
        this.workSpaceService = workSpaceService;
    }

    @RequestMapping("/create")
    public ResponseEntity<APIResponse<WorkSpaceDto>> createWorkSpace(@Valid @RequestBody WorkSpaceDto workSpace, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        WorkSpaceDto workSpaceDto = workSpaceService.createWorkSpace(workSpace, user);
        APIResponse<WorkSpaceDto> response = new APIResponse<>("201", "WorkSpace created and linked to user", workSpaceDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<WorkSpaceDto>> deleteWorkSpace(@PathVariable String id) {

        WorkSpaceDto workSpaceDto = workSpaceService.deleteWorkSpace(id, true);

        APIResponse<WorkSpaceDto> response = new APIResponse<>("200", "Workspace marked as deleted", workSpaceDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

   @RequestMapping( "/get/{id}")
    public ResponseEntity<APIResponse<WorkSpaceDto>> getById(@PathVariable String id, Authentication authentication) {
       User user = userContextService.getCurrentUser(authentication);

       WorkSpaceDto workSpaceDto = workSpaceService.retriveWorkSpace(id, user);
       APIResponse<WorkSpaceDto> response = new APIResponse<>("200", "Workspace retrieved successfully", workSpaceDto);

       return  new ResponseEntity<>(response, HttpStatus.OK);
    }

   @GetMapping("/getAll")

    public ResponseEntity<ApiResponsePage<List<WorkSpaceDto>>> getAll(Authentication authentication, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize , @RequestParam(defaultValue = "name") String sortField, @RequestParam(defaultValue = "asc") String sortDir) {
       Page<WorkSpaceDto> workSpaceDtos = workSpaceService.retriveAllWorkSpace(authentication,pageNum,pageSize,sortField,sortDir);
       ApiResponsePage<List<WorkSpaceDto>> response = new ApiResponsePage<>("200", "Workspaces retrieved successfully", workSpaceDtos.getContent(),workSpaceDtos.getNumber()+1,workSpaceDtos.getTotalPages(),
               workSpaceDtos.getTotalElements());
        return   new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<WorkSpaceDto>> updateWorkSpace(@PathVariable String id,  @RequestBody WorkSpaceDto workSpace, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);

        WorkSpaceDto workSpaceDto = workSpaceService.updateWorkSpace(id, workSpace  ,user);
        APIResponse<WorkSpaceDto> response = new APIResponse<>("200", "Workspace Updated successfully", workSpaceDto);

        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
//بيرجع تلقائيًا HTTP 403 Forbidden من غير ما يدخل جسم الدالة.
  //@PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/getAllWorkSpace")

    public ResponseEntity<ApiResponsePage<List<WorkSpaceDto>>> getAllWorkSpace(Authentication authentication,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize ,@RequestParam(defaultValue = "name") String sortField,@RequestParam(defaultValue = "asc") String sortDir) {
       roleServices.checkRole(authentication);
        User user = userContextService.getCurrentUser(authentication);
        Page<WorkSpaceDto> workSpaceDtos=workSpaceService.retriveAllWorkSpaceAllUser(user,pageNum,pageSize,sortField,sortDir);
        ApiResponsePage<List<WorkSpaceDto>> response = new ApiResponsePage<>("200", "Workspaces retrieved successfully", workSpaceDtos.getContent(),workSpaceDtos.getNumber()+1,workSpaceDtos.getTotalPages(),
                workSpaceDtos.getTotalElements());
        return   new ResponseEntity<>(response, HttpStatus.OK);
    }
}
