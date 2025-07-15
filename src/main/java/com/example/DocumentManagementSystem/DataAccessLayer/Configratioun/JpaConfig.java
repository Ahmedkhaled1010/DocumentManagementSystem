package com.example.DocumentManagementSystem.DataAccessLayer.Configratioun;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.DocumentManagementSystem.DataAccessLayer.Repository.jpa"
)
@EntityScan("com.example.DocumentManagementSystem.DataAccessLayer.Models")
public class JpaConfig {

}
