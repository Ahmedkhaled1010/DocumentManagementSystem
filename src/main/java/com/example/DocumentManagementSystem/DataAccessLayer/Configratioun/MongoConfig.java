package com.example.DocumentManagementSystem.DataAccessLayer.Configratioun;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.DocumentManagementSystem.DataAccessLayer.Repository.mongo")
@EnableMongoAuditing
public class MongoConfig {

}
