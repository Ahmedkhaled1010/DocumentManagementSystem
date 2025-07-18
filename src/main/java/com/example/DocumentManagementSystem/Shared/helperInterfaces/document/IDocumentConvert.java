package com.example.DocumentManagementSystem.Shared.helperInterfaces.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import org.springframework.web.multipart.MultipartFile;

public interface IDocumentConvert {
    Document createDocument(WorkSpace workSpaceId, MultipartFile file, String filePath);
}
