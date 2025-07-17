package com.example.DocumentManagementSystem.Shared.helperClasses.workspace;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Documnet;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo.DocumentRepository;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.workspace.IWorkSpaceDeletionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkSpaceDeletionService implements IWorkSpaceDeletionService {
    private final MongoTemplate mongoTemplate;
    private final DocumentRepository documentRepository;

    @Autowired
    public WorkSpaceDeletionService(MongoTemplate mongoTemplate, DocumentRepository documentRepository) {
        this.mongoTemplate = mongoTemplate;
        this.documentRepository = documentRepository;
    }

    @Override
    public void softDeleteWorkSpace(WorkSpace workspace) {
        Query query = new Query(Criteria.where("_id").is(workspace.getId()));
        Update update = new Update().set("isDeleted", true);
        mongoTemplate.updateFirst(query, update, WorkSpace.class);
//هناك تعديل
        List<Documnet> documents = workspace.getDocuments();
        if (documents != null && !documents.isEmpty()) {
            for (Documnet document : documents) {
                document.setIsDeleted(true);
                documentRepository.save(document);
            }
        }
    }
}
