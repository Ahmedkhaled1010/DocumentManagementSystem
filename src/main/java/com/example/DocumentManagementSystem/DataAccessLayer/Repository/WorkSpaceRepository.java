package com.example.DocumentManagementSystem.DataAccessLayer.Repository;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.WorkSpace;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkSpaceRepository extends MongoRepository<WorkSpace,String> {
}
