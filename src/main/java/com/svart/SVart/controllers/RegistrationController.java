package com.svart.SVart.controllers;

import com.svart.SVart.Repository.UserRepository;
import com.svart.SVart.entity.Role;
import com.svart.SVart.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

@Controller
public class  RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration() {
        return "/registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Map<String, Object> model) {
           User userFromDb = userRepository.findByUsername(user.getUsername());

           if(userFromDb !=null){
              model.put("message", "User exists!");
              return "registration";
           }

           user.setActive(true);
           user.setRoles(Collections.singleton(Role.User));
           userRepository.save(user);

        return "redirect:/login";
    }

}
