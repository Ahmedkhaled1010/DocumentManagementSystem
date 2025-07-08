package com.example.DocumrntManagementSystem.BusinessLayer;

import com.example.DocumrntManagementSystem.DataAccessLayer.Models.Role;
import com.example.DocumrntManagementSystem.DataAccessLayer.Models.User;
import com.example.DocumrntManagementSystem.DataAccessLayer.Repository.RolesRepository;
import com.example.DocumrntManagementSystem.DataAccessLayer.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServices {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RolesRepository rolesRepository;
    public boolean createUser(User user)
    {
            boolean isSaved =false;


            if (userRepository.findByEmail(user.getEmail())!=null) {
               throw new RuntimeException("User already exists");
            }
            if (userRepository.findByUserName(user.getUserName())!=null)
              {
            throw new RuntimeException(" Already taken");
                }
            Role role = rolesRepository.getByRoleName("USER");
            log.info("Role is {}",role);
            user.setRole(role);
            User result = userRepository.save(user);

            if(result!=null && result.getUserId()>0)
            {
                isSaved=true;
            }
            return isSaved;
    }
}
