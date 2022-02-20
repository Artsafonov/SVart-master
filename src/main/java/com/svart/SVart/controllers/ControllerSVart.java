package com.svart.SVart.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ControllerSVart {

    @GetMapping("/")
    public String home (Model model) {
        model.addAttribute("title", "Home page");
        return "home";
    }


    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About");
        return "about";
    }

    @GetMapping("/contacts")
    public String contacts(Model model) {
        model.addAttribute("title", "contacts");
        return "contacts";
    }

}


