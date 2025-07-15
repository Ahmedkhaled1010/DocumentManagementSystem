package com.example.DocumentManagementSystem.PresentationLayer.Controller;

import com.example.DocumentManagementSystem.BusinessLayer.Services.DocumentServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
@Slf4j
public class DocumentController {
    @Autowired
    DocumentServices documentServices;

    @PostMapping("/create")
    public Documnet createDocument(@RequestBody Documnet document) {
        log.info(document.toString());
        return documentServices.createDocument(document);
    }
}
