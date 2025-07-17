package com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkSpaceRepository extends MongoRepository<WorkSpace, ObjectId> {

    @Update("{ '$set': { 'isDeleted': ?1 } }")
    @Query("{ '_id': ?0 }")
    long updateByIsDeleted ( ObjectId workId, Boolean isDeleted);
   // Optional<WorkSpace> findById(String id);
    Page<WorkSpace> findByUserNationalID(String userNationalID, Pageable pageable);
}
