package com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Document;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<Document, ObjectId>{

    List<Document> findByNationalIDAndIsDeleted(String nationalID, boolean isDeleted);
    Page<Document> findByWorkspaceId(ObjectId workSpaceId, Pageable pageable);
    Document findByNationalID(String nationalID);
}
