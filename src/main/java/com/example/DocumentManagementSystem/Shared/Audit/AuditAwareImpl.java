package com.example.DocumentManagementSystem.Shared.Audit;

import com.example.DocumentManagementSystem.DataAccessLayer.Models.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String>  getCurrentAuditor() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            return Optional.ofNullable(user.getUserName());
        }

        return Optional.empty();    }

}
