package com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.UserWorkSpace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserWorkSpaceRepository extends JpaRepository<UserWorkSpace, Long> {
List<UserWorkSpace> findByUserId(UUID userId);
}
