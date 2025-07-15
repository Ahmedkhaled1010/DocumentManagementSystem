package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.DocumentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@Slf4j
public class DocumentServices {

    @Autowired
    DocumentRepository documentRepository;

    public Documnet createDocument( Documnet document) {
        log.info(document.getTitle());
        return documentRepository.save(document);
    }

    public ResponseEntity<String> uploadFile() {
        return null;
    }
}
