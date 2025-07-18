package com.example.DocumentManagementSystem.Shared.helperClasses.document;

import com.example.DocumentManagementSystem.Shared.helperInterfaces.document.IFileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
@Service
public class FileStorageService implements IFileStorageService {
    @Value("${app.document.directory}")
    private String directory;
    @Override
    public String saveFile(MultipartFile file) throws IOException {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String filePath = directory + file.getOriginalFilename();
        file.transferTo(new File(filePath));
        return filePath;
    }
}
