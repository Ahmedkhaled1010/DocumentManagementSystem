package com.example.DocumentManagementSystem.Shared.helperClasses.document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.Shared.helperInterfaces.document.IDocumentDeletionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class DocumentDeletionServices implements IDocumentDeletionServices {
    private  final MongoTemplate mongoTemplate;
    @Autowired
    public DocumentDeletionServices(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public void softDeleteWorkSpace(Document documnet, boolean isDeleted) {
        Query query = new Query(Criteria.where("_id").is(documnet.getId()));
        Update update = new Update().set("isDeleted", isDeleted);
        mongoTemplate.updateFirst(query, update, Document.class);
    }
}
