package com.example.DocumentManagementSystem.Shared.helperInterfaces.document;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileStorageService {
    public String saveFile(MultipartFile file) throws IOException;
}
