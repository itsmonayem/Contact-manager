package com.spring.contactmanager.controller;

import com.spring.contactmanager.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title","Home - Contact Manager");
        return "home";
    }




    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title","About - Contact Manager");
        return "about";
    }



    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title","Register - Contact Manager");
        model.addAttribute("user",new User());
        return "signup";
    }



//    handdler for register user
    @PostMapping("/do_register")
    public String registerUser(@ModelAttribute User user, @RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model) {
        if (!agreement) {
            System.out.println("You have not agreed");
            model.addAttribute("user",user);
            return "signup";
        }
        user.setRole("ROLE_USER");
        user.setEnabled(true);

        return "signup";
    }












//    @GetMapping("/about")
//    public String about(Model model) {
//        model.addAttribute("title","About - Contact Manager");
//        return "about";
//    }
}
