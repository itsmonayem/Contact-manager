package com.spring.contactmanager.controller;

import com.spring.contactmanager.dao.UserRepository;
import com.spring.contactmanager.entities.User;
import com.spring.contactmanager.helper.Message;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserRepository userRepository;


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home - Contact Manager");
        return "home";
    }


    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About - Contact Manager");
        return "about";
    }


    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register - Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }







    //    handdler for register user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult result,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {

        try {
            if (!agreement) {
                System.out.println("You have not agreed");
                throw new Exception("You have not agreed with terms and condition");
            }
            if (result.hasErrors()) {
                model.addAttribute("user",user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setPassword(this.passwordEncoder.encode(user.getPassword()));

            model.addAttribute("user", new User());
            model.addAttribute("message", new Message("Registration Successful", "alert-success"));

            this.userRepository.save(user);
            return "signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            model.addAttribute("message", new Message("Something went wrong" + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }



    //Handler for custom login
    @GetMapping("/login")
    public String customLogin(Model model) {
        model.addAttribute("title","Login Page");
        return "login";
    }




}
