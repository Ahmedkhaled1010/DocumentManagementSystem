package com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Role, Integer> {

    Role getByRoleName(String roleName);
}
