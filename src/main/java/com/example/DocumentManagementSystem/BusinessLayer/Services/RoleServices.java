package com.example.DocumentManagementSystem.BusinessLayer.Services;

import com.example.DocumentManagementSystem.BusinessLayer.Interfaces.IRoleServices;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa.RolesRepository;
import com.example.DocumentManagementSystem.Exception.Exceptions.UnauthorizedAccessException;
import com.example.DocumentManagementSystem.Shared.Enum.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class RoleServices implements IRoleServices {
    private final RolesRepository rolesRepository;
    @Autowired
    public RoleServices(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    @Override
    public User assignRole(User user) {
        Role role = rolesRepository.getByRoleName(Roles.ADMIN.name());
        user.setRole(role);
        return user;
    }
    public void checkRole(Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new UnauthorizedAccessException("Access denied. Admins only.");
        }

    }

}
