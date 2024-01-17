package com.spring.contactmanager.controller;

import com.spring.contactmanager.dao.UserRepository;
import com.spring.contactmanager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println(userName);

        //Ger User details
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println(user);

        model.addAttribute("user",user);


        return "public/user_dashboard";
    }
}
