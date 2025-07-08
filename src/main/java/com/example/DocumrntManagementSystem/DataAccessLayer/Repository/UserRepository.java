package com.example.DocumrntManagementSystem.DataAccessLayer.Repository;

import com.example.DocumrntManagementSystem.DataAccessLayer.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByEmail(String email);
    User findByUserName(String username);

}
