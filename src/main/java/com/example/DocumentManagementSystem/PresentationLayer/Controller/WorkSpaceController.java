package com.example.DocumentManagementSystem.PresentationLayer.Controller;


import com.example.DocumentManagementSystem.BusinessLayer.Services.WorkSpaceService;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/workspace")
@Slf4j

public class WorkSpaceController {
    @Autowired
    WorkSpaceService workSpaceService;

    @RequestMapping("/create")
    public ResponseEntity<APIResponse<Map<String, Object>>> createWorkSpace(@Valid @RequestBody WorkSpaceDto workSpace, Authentication authentication, BindingResult bindingResult) {


        return workSpaceService.createWorkSpace(workSpace, authentication, bindingResult);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<APIResponse<Object>> deleteWorkSpace(@PathVariable String id) {


        return workSpaceService.deleteWorkSpace(id, false);
    }

    @RequestMapping( "/get/{id}")
    public ResponseEntity<APIResponse<Object>> getById(@PathVariable String id, Authentication authentication) {
        return workSpaceService.retriveWorkSpace(id, authentication);
    }

    @GetMapping("/getAll")

    public ResponseEntity<?> getAll(Authentication authentication,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize ,@RequestParam(defaultValue = "name") String sortField,@RequestParam(defaultValue = "asc") String sortDir) {
        return workSpaceService.retriveAllWorkSpace(authentication,pageNum,pageSize,sortField,sortDir);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<APIResponse<Object>> updateWorkSpace(@PathVariable String id,  @RequestBody WorkSpaceDto workSpace, Authentication authentication) {
        return workSpaceService.updateWorkSpace(id, workSpace, authentication);
    }
    @PreAuthorize("hasRole('ADMIN')")

    @GetMapping("/getAllWorkSpace")

    public ResponseEntity<?> getAllWorkSpace(Authentication authentication,@RequestParam(defaultValue = "1") int pageNum,@RequestParam(defaultValue = "10") int pageSize ,@RequestParam(defaultValue = "name") String sortField,@RequestParam(defaultValue = "asc") String sortDir) {
        return workSpaceService.retriveAllWorkSpaceAllUser(authentication,pageNum,pageSize,sortField,sortDir);
    }
}
