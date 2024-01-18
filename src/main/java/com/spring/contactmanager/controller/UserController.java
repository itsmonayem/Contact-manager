package com.spring.contactmanager.controller;

import com.spring.contactmanager.dao.ContactRepository;
import com.spring.contactmanager.dao.UserRepository;
import com.spring.contactmanager.entities.Contact;
import com.spring.contactmanager.entities.User;
import com.spring.contactmanager.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println(userName);

        //Ger User details
        User user = this.userRepository.getUserByUserName(userName);
        System.out.println(user);

        model.addAttribute("user", user);
    }


    //    Dashboard home
    @GetMapping("/index")
    public String dashboard(Model model, Principal principal) {
        return "public/user_dashboard";
    }


    //    open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());

        return "public/add-contact";
    }



//    processing add contact form
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("contact_image") MultipartFile file, Principal principal, Model model) {

        try{
            String userName = principal.getName();
            User user = this.userRepository.getUserByUserName(userName);

            //Processing and uploading file
            if(file.isEmpty()) {
                System.out.println("file is Empty");
            } else {
                contact.setImage(file.getOriginalFilename());
                File saveFile = new ClassPathResource("static/images").getFile();
                Path path =  Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image successfully uploaded");
            }

            contact.setUser(user);
            user.getContacts().add(contact);
            this.userRepository.save(user);

            System.out.println(contact);

            //message success
            model.addAttribute("message",new Message("Contact add successfully!!","alert-success"));


        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            model.addAttribute("message",new Message("Sorry Try again","alert-danger"));

            e.printStackTrace();
        }

        return "public/add-contact";
    }




//    Show contacts
//    per page 5 contacts
//    current page 0
    @GetMapping("/show-contacts/{page}")
    public String showContacts(@PathVariable Integer page, Model model, Principal principal){
        model.addAttribute("title","Show Contacts");

        // show contacts list
        String username = principal.getName();
        User user = this.userRepository.getUserByUserName(username);

        Pageable pageable = PageRequest.of(page, 5);
        Page<Contact> contactList = this.contactRepository.findContactsByUser(user.getId(), pageable);

        model.addAttribute("contacts",contactList);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages",contactList.getTotalPages());

        System.out.println(contactList);

        return "public/show-contacts";
    }




    //Deleting aa contact
    @GetMapping("/delete/{cId}")
    public String deleteContact(@PathVariable("cId") int cId, Principal principal) {
        Optional<Contact> optionalContact = this.contactRepository.findById(cId);
        Contact contact = optionalContact.get();

        //Check deleting the contact by the owner
        User user = this.userRepository.getUserByUserName(principal.getName());
        if(contact.getUser().getId() != user.getId()) {
            System.out.println("You can not delete the user");
        } else {
            user.getContacts().remove(contact);
            this.userRepository.save(user);
        }

        return "redirect:/user/show-contacts/0";
    }


}
