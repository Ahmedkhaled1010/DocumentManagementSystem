package com.example.DocumentManagementSystem.BusinessLayer.Interfaces;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentDto;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentSearchCriteria;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IDocumentServices {
    List<DocumentDto> createDocument(String workSpaceId, MultipartFile[] files);
    Resource downloadDocument(Document document, User user) throws IOException;
    DocumentDto changeStatusDocument(String documentId, User user, boolean isDeleted);
    String deleteDocument(String documentId,User user);
    DocumentDto updateDocument(String documentId,DocumentDto documentDto,User user);
    DocumentDto getDocument(String documentId,User user);
    Page<DocumentDto> getAllDocuments(User user, DocumentSearchCriteria filters);
}
