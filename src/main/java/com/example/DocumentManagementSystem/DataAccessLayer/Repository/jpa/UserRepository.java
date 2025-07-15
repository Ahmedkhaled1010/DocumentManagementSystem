package com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.rmi.server.UID;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserName(String username);
    Optional<User> findByuserId(UUID userId);
    Optional<User> findByNationalID(String nationalID);

}
