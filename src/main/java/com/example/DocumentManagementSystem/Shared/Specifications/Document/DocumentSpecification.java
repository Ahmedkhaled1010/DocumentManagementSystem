package com.example.DocumentManagementSystem.Shared.Specifications.Document;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import com.example.DocumentManagementSystem.Shared.DataTransferModel.DocumentDto.DocumentSearchCriteria;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class DocumentSpecification {
    private final MongoTemplate mongoTemplate;
    @Autowired
    public DocumentSpecification(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Page<Document> filterDocuments(DocumentSearchCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPageNum() - 1,
                criteria.getPageSize(),
                criteria.getSortDirection().equalsIgnoreCase("asc")
                        ? Sort.by(criteria.getSortField()).ascending()
                        : Sort.by(criteria.getSortField()).descending()
        );

        Query query = new Query().with(pageable);
        query.addCriteria(Criteria.where("isDeleted").is(false)); // تجاهل المحذوف

        if (criteria.getFileName() != null && !criteria.getFileName().isEmpty()) {
            query.addCriteria(Criteria.where("fileName").regex(criteria.getFileName(), "i"));
        }
        if (criteria.getSize() != 0) {
            query.addCriteria(Criteria.where("size").is(criteria.getSize()));
        }
        if (criteria.getFileType() != null) {
            query.addCriteria(Criteria.where("fileType").is(criteria.getFileType()));
        }

        if (criteria.getNationalID() != null) {
            query.addCriteria(Criteria.where("nationalID").is(criteria.getNationalID()));
        }

        if (criteria.getTag() != null) {
            query.addCriteria(Criteria.where("tag").is(criteria.getTag()));
        }

        if (criteria.getPrivacy() != null) {
            query.addCriteria(Criteria.where("privacy").is(criteria.getPrivacy()));
        }

        if (criteria.getWorkspaceId() != null) {
            query.addCriteria(Criteria.where("workspaceId").is(new ObjectId(criteria.getWorkspaceId())));
        }

        List<Document> documents = mongoTemplate.find(query, Document.class);
        long total = mongoTemplate.count(query.skip(-1).limit(-1), Document.class);

        return new PageImpl<>(documents, pageable, total);
    }



}
