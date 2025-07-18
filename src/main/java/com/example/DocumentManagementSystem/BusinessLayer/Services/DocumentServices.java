package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.BusinessLayer.Interfaces.IDocumentServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.UserRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.DocumentRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.DocumentDeletedException;
import com.example.DocumentManagementSystem.Exception.Exceptions.FileStorageException;
import com.example.DocumentManagementSystem.Exception.Exceptions.ResourceNotFoundException;
import com.example.DocumentManagementSystem.Exception.Exceptions.UnauthorizedAccessException;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentSearchCriteria;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.WorkSpace.WorkSpaceDto;
import com.example.DocumentManagementSystem.Shared.Specifications.Document.DocumentSpecification;
import com.example.DocumentManagementSystem.Shared.helperClasses.document.DocumentBase64;
import com.example.DocumentManagementSystem.Shared.helperClasses.document.DocumentConvert;
import com.example.DocumentManagementSystem.Shared.helperClasses.document.DocumentDeletionServices;
import com.example.DocumentManagementSystem.Shared.helperClasses.document.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class DocumentServices implements IDocumentServices {
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final DocumentConvert documentConvert;
    private final FileStorageService fileStorageService;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;
    private final WorkSpaceService workSpaceService;
    private final DocumentSpecification documentSpecification;
    private final DocumentDeletionServices documentDeletionServices;
    private final DocumentBase64 documentBase64;

    public DocumentServices(DocumentRepository documentRepository,
                            UserRepository userRepository,
                            DocumentConvert documentConvert,
                            ModelMapper modelMapper,
                            MongoTemplate mongoTemplate,
                            WorkSpaceService workSpaceService,
                            DocumentSpecification documentSpecification,
                            FileStorageService fileStorageService,
                            DocumentDeletionServices documentDeletionServices,
                            DocumentBase64 documentBase64) {
        this.documentRepository = documentRepository;
        this.fileStorageService = fileStorageService;
        this.userRepository = userRepository;
        this.documentConvert = documentConvert;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
        this.workSpaceService = workSpaceService;
        this.documentSpecification = documentSpecification;
        this.documentDeletionServices = documentDeletionServices;
        this.documentBase64 = documentBase64;
    }

    public List<DocumentDto> createDocument(String workSpaceId, MultipartFile[] files) {
        WorkSpace workSpace = workSpaceService.findWorkSpaceById(workSpaceId);
        if (workSpace.getDocuments() == null) {
            workSpace.setDocuments(new ArrayList<>());
        }

        List<Document> documents = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String filePath = fileStorageService.saveFile(file);
                Document document = documentConvert.createDocument(workSpace, file, filePath);
                document = documentRepository.save(document);
                workSpace.getDocuments().add(document);
                documents.add(document);
            } catch (IOException e) {
                throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
            }
        }
        workSpaceService.updateWorkSpace(workSpace);
        List<DocumentDto> documentDtos = documents.stream().map(document -> modelMapper.map(document, DocumentDto.class)).toList();
        return documentDtos;
    }

    public Resource downloadDocument(Document document, User user) throws IOException {

        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        Document doc = document;
        Path path = Paths.get(doc.getFilePath());
        if (!Files.exists(path)) {
            throw new ResourceNotFoundException("File not found");
        }
        Resource resource = new UrlResource(path.toUri());
        return resource;
    }


    public DocumentDto changeStatusDocument(String documentId, User user, boolean isDeleted) {
        Document document = findDocumentById(documentId);
        isDocumentDeleted(document, isDeleted);
        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        documentDeletionServices.softDeleteWorkSpace(document, isDeleted);
        document = findDocumentById(documentId);
        DocumentDto documentDto = modelMapper.map(document, DocumentDto.class);
        return documentDto;
    }

    public String previewDocument(String documentId, User user) throws IOException {
        Document document = findDocumentById(documentId);
        isDocumentDeleted(document, true);
        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        Document doc = document;
        String base64WithMime = documentBase64.convertToBase64(doc);
        return base64WithMime;


    }

    public String deleteDocument(String documentId, User user) {
        Document document = findDocumentById(documentId);
        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        documentRepository.deleteById(document.getId());
        return isDocumentNotDeleted(document);
    }

    public DocumentDto updateDocument(String documentId, DocumentDto documentDto, User user) {
        Document document = findDocumentById(documentId);
        isDocumentDeleted(document, true);
        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        updateDocumentFromDto(document, documentDto);
        document = documentRepository.save(document);
        DocumentDto documentDto1 = modelMapper.map(document, DocumentDto.class);
        return documentDto1;
    }

    public DocumentDto getDocument(String documentId, User user) {
        Document document = findDocumentById(documentId);
        isDocumentDeleted(document, true);
        isUserAuthorizedForDocument(user.getNationalID(), document.getId().toString());
        DocumentDto documentDto = modelMapper.map(document, DocumentDto.class);

        return documentDto;
    }

    public Page<DocumentDto> getAllDocuments(User user, DocumentSearchCriteria filters) {




        Page<Document> documents = documentSpecification.filterDocuments(filters);
        Page<DocumentDto> documentDtoPage = convertToDtoList(documents);

        if (documentDtoPage.isEmpty()) {
            throw new ResourceNotFoundException("No documents found for the given filters");
        }



        return documentDtoPage;
    }

   /* public Page<DocumentDto> getAllDocumentsByWorkSpace(String workSpaceId,User user, DocumentSearchCriteria filters)  {

         WorkSpace workSpace = workSpaceService.findWorkSpaceById(workSpaceId);
         workSpaceService.isUserAuthorizedForWorkspace(user.getUserId(), workSpaceId);


         Page<Documnet> documents=documentSpecification.specificDocument(workSpaceId,null,filters,pageable);
         if (documents.isEmpty()) {
             response = new ApiResponsePage<>("204", "NO_CONTENT", Map.of("message", "No documents found"), documents.getNumber() + 1, documents.getTotalPages(), documents.getTotalElements());
             return ResponseEntity.ok(response);
         }
             List<DocumentDto> documentDtos = documents.getContent().stream().map(document -> modelMapper.map(document, DocumentDto.class)).toList();
             Page<DocumentDto> documentDtoPage = new PageImpl<>(documentDtos, pageable, documents.getTotalElements());
             response = new ApiResponsePage<>("200", "Success", documentDtoPage.getContent(), documents.getNumber() + 1, documents.getTotalPages(), documents.getTotalElements());
             return ResponseEntity.ok(response);
     }
*/
    public Document findDocumentById(String documentId) {
        return documentRepository.findById(new ObjectId(documentId))
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    private void isUserAuthorizedForDocument(String nationalID, String documentId) {
        boolean isAuthorized = documentRepository.findById(new ObjectId(documentId))
                .map(document -> document.getNationalID().equals(nationalID))
                .orElse(false);

        if (!isAuthorized) {
            throw new UnauthorizedAccessException("User is not authorized to access this Document");
        }
    }

    private void isDocumentDeleted(Document documnet, boolean isDeleted) {
        if (isDeleted) {
            if (documnet.getIsDeleted()) {
                throw new DocumentDeletedException("Document is deleted");
            }
        }
    }

    private String isDocumentNotDeleted(Document documnet) {
        if (documentRepository.existsById(documnet.getId())) {
            throw new DocumentDeletedException("Error deleting document");
        }
        return "Document deleted successfully";
    }

    private void updateDocumentFromDto(Document document, DocumentDto documentDto) {
        if (documentDto.getFileName() != null) {
            document.setFileName(documentDto.getFileName());
        }
        if (documentDto.getFileType() != null) {
            document.setFileType(documentDto.getFileType());
        }
        if (documentDto.getFilePath() != null) {
            document.setFilePath(documentDto.getFilePath());
        }
        if (documentDto.getSize() != 0) {
            document.setSize(documentDto.getSize());
        }

        if (documentDto.getPrivacy() != null) {
            document.setPrivacy(documentDto.getPrivacy());
        }

    }

    private Pageable buildPageRequest(int pageNum, int pageSize, String sortField, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        return PageRequest.of(pageNum - 1, pageSize, sort);
    }
    private Page<DocumentDto> convertToDtoList(Page<Document> documents) {
        List<DocumentDto> documentDtos = documents.stream()
                .map(ws -> modelMapper.map(ws, DocumentDto.class))
                .toList();;
        return new PageImpl<>(documentDtos, documents.getPageable(), documents.getTotalElements());
    }
}
