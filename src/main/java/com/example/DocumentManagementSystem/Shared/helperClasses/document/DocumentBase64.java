package com.example.DocumentManagementSystem.Shared.helperClasses.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.Exception.Exceptions.ResourceNotFoundException;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.document.IDocumentBase64;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@Service
public class DocumentBase64 implements IDocumentBase64 {

    @Override
    public String convertToBase64(Document documnet) throws IOException {
        Path path = Paths.get(documnet.getFilePath()).toAbsolutePath();
        if (!Files.exists(path)) {
            throw new ResourceNotFoundException("File not found");
        }

        byte[] fileContent = Files.readAllBytes(path);
        String base64Encoded = Base64.getEncoder().encodeToString(fileContent);
        String mimeType = Files.probeContentType(path);
        String base64WithMime = "data:" + mimeType + ";base64," + base64Encoded;
        return base64WithMime;
    }
}
