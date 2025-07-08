package com.example.DocumrntManagementSystem.PresentationLayer.Controller;

import com.example.DocumrntManagementSystem.BusinessLayer.UserServices;
import com.example.DocumrntManagementSystem.DataAccessLayer.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserServices userServices;

    @RequestMapping("/create")
    public boolean createUser(@RequestBody User user) {


        return userServices.createUser(user);
    }
}
