package com.example.DocumentManagementSystem.Shared.helperClasses.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.DocumentRepository;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.WorkSpaceRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Optional;

@Component
public  class convertFileToDocument {

    @Autowired
    WorkSpaceRepository workSpaceRepository;
    @Autowired
    DocumentRepository documentRepository;
    public    Documnet createDocument(String workSpaceId, MultipartFile file, String filePath){
        Optional<WorkSpace> workSpace =workSpaceRepository.findById(new ObjectId(workSpaceId));
        Documnet document = new Documnet();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(filePath);
        document.setFileType(file.getContentType());
        document.setSize(file.getSize() / 1024);//KB

        if (workSpace.isPresent()) {
            document.setWorkspaceId(new ObjectId(workSpaceId));
            document.setNationalID(workSpace.get().getUserNationalID());

            document=documentRepository.save(document);
            if (workSpace.get().getDocuments() == null) {
                workSpace.get().setDocuments(new ArrayList<>());
            }
            workSpace.get().getDocuments().add(document);
            workSpaceRepository.save(workSpace.get());



        }

        return document;

    }
}
