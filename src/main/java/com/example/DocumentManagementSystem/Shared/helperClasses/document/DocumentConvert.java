package com.example.DocumentManagementSystem.Shared.helperClasses.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.document.IDocumentConvert;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public  class DocumentConvert implements IDocumentConvert {


    public Document createDocument(WorkSpace workSpace, MultipartFile file, String filePath){
        Document document = new Document();
        document.setFileName(file.getOriginalFilename());
        document.setFilePath(filePath);
        document.setFileType(file.getContentType());
        document.setSize(file.getSize() / 1024);//KB
        document.setWorkspaceId(workSpace.getId());
        document.setNationalID(workSpace.getUserNationalID());


        return document;

    }
}
