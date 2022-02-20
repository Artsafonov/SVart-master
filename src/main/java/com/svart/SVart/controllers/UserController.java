package com.svart.SVart.controllers;

import com.svart.SVart.Repository.UserRepository;
import com.svart.SVart.entity.Role;
import com.svart.SVart.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;


@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private Model model;

    @GetMapping("/user")
    public String userList(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/user/{id}")
    public String userDetails(@PathVariable(value = "id") long id, Model model) {
        if (!userRepository.existsById(id)) {  // !postRepository.existsById(id) если такого ID нет (False), Знак ! это делает//
            return "redirect:/user";
        }
        Optional<User> user = userRepository.findById(id);
        ArrayList<User> result = new ArrayList<>();
        user.ifPresent(result::add);
        model.addAttribute("user", result);
        model.addAttribute("roles" , Role.values());
        return "userEdit";
    }

    @GetMapping("/user/{id}/edit")
    public String userEdit(@PathVariable(value = "id") long id, Model model) {
        if (!userRepository.existsById(id)) {  // !postRepository.existsById(id) если такого ID нет (False), Знак ! это делает//
            return "redirect:/user";
        }
        Optional<User> post = userRepository.findById(id);
        ArrayList<User> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("user", result);
        model.addAttribute("roles" , Role.values());
        return "userEdit";
    }

    @PostMapping("/user/{id}")
    public String userUpdate(@PathVariable(value = "id") long id, @RequestParam String username, @RequestParam String password, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/user";
    }

    @PostMapping("/user/{id}/remove")
    public String userDelete(@PathVariable(value = "id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        userRepository.delete(user);
        return "redirect:/user";
    }


}