package com.example.DocumentManagementSystem.PresentationLayer.Controller;

import com.example.DocumentManagementSystem.BusinessLayer.Services.DocumentServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentSearchCriteria;
import com.example.DocumentManagementSystem.Shared.POJO.APIResponse;
import com.example.DocumentManagementSystem.Shared.POJO.ApiResponsePage;
import com.example.DocumentManagementSystem.Shared.helperClasses.User.UserContextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/document")
@Slf4j
public class DocumentController {
    private final DocumentServices documentServices;
    private final UserContextService userContextService;

    @Autowired
    public DocumentController(DocumentServices documentServices,
                              UserContextService userContextService) {
        this.documentServices = documentServices;
        this.userContextService = userContextService;
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<List<DocumentDto>>> createDocument(@RequestParam String workSpaceId, @RequestParam MultipartFile[] files) {
        List<DocumentDto> documentDtos = documentServices.createDocument(workSpaceId, files);

        APIResponse<List<DocumentDto>> response = new APIResponse<>("200", "Documents created successfully", documentDtos);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/download/{documentId}")
    public ResponseEntity<Resource> downloadDocument(@PathVariable String documentId, Authentication authentication) throws IOException {
        User user = userContextService.getCurrentUser(authentication);
        Document document = documentServices.findDocumentById(documentId);
        Resource resource = documentServices.downloadDocument(document, user);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{documentId}")
    public ResponseEntity<APIResponse<DocumentDto>> deleteDocument(@PathVariable String documentId, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);
        DocumentDto documentDto = documentServices.changeStatusDocument(documentId, user, true);
        APIResponse<DocumentDto> response = new APIResponse<>("200", "Document Marked as Deleted successfully", documentDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/review/{documentId}")
    public ResponseEntity<APIResponse<String>> reviewDocument(@PathVariable String documentId, Authentication authentication) throws IOException {
        User user = userContextService.getCurrentUser(authentication);
        String base64WithMime = documentServices.previewDocument(documentId, user);
        APIResponse<String> response = new APIResponse<>("200", "Documents Review", base64WithMime);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deletedata/{documentId}")
    public ResponseEntity<APIResponse<String>> deletedataDocument(@PathVariable String documentId, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);
        String message = documentServices.deleteDocument(documentId, user);
        APIResponse<String> response = new APIResponse<>("200", "Document Deleted Successfully", message);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/retrive/{documentId}")
    public ResponseEntity<APIResponse<DocumentDto>> retriveDocument(@PathVariable String documentId, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);
        DocumentDto documentDto = documentServices.changeStatusDocument(documentId, user, false);
        APIResponse<DocumentDto> response = new APIResponse<>("200", "Document Marked as Not Deleted successfully", documentDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/updatedata/{documentId}")
    public ResponseEntity<APIResponse<DocumentDto>> updatedataDocument(@PathVariable String documentId, @RequestBody DocumentDto document, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);
        DocumentDto documentDto = documentServices.updateDocument(documentId, document, user);

        APIResponse<DocumentDto> response = new APIResponse<>("200", "Document Updated successfully", documentDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/getdata/{documentId}")
    public ResponseEntity<APIResponse<DocumentDto>> getdataDocument(@PathVariable String documentId, Authentication authentication) {
        User user = userContextService.getCurrentUser(authentication);
        DocumentDto documentDto = documentServices.getDocument(documentId, user);
        APIResponse<DocumentDto> response = new APIResponse<>("200", "Document Fetched successfully", documentDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


   @GetMapping("/all")
    public ResponseEntity<ApiResponsePage<List<DocumentDto>>> getAllDocuments(Authentication authentication, @ModelAttribute DocumentSearchCriteria documentSearchCriteria) {
        User user = userContextService.getCurrentUser(authentication);
       Page<DocumentDto> documentDtos = documentServices.getAllDocuments(user,documentSearchCriteria);
       ApiResponsePage<List<DocumentDto>> response = new ApiResponsePage<>("200", "Documents retrieved successfully", documentDtos.getContent(),documentDtos.getNumber()+1,documentDtos.getTotalPages(),
               documentDtos.getTotalElements());
       return   new ResponseEntity<>(response, HttpStatus.OK);
    }




   /* @GetMapping("/all/{workSpaceId}")
    public ResponseEntity<?>  GetAllDocumentsByWorkSpace (@PathVariable String workSpaceId, Authentication authentication, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize , @RequestParam(defaultValue = "name") String sortField, @RequestParam(defaultValue = "asc") String sortDir ,@RequestParam Map<String, Object> allParams) throws IOException {

        return  documentServices.getAllDocumentsByWorkSpace(workSpaceId,authentication,pageNum,pageSize,sortField,sortDir,allParams);
    }*/






}
