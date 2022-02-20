package com.svart.SVart.controllers;


import com.svart.SVart.Repository.PostRepository;
import com.svart.SVart.entity.User;
import com.svart.SVart.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
public class BlogController {
    @Autowired
    private PostRepository postRepository;
    private Model model;
    @Value("${upload.path}") //ищет в aplication.properties upload.path и вставляет его в переменную String uploadPath
    private String uploadPath;

    @GetMapping("/blog")
    public String blog(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Post> posts = postRepository.findAll();
        if (filter != null && !filter.isEmpty()) {
            posts = postRepository.findByTitle(filter);
        } else {
            posts = postRepository.findAll();
        }
        model.addAttribute("filter", filter);
        model.addAttribute("posts", posts);
        return "blog-main";
    }



    @PostMapping("/blog")
    public String blogPostAdd(
            @AuthenticationPrincipal User user,
            @RequestParam String title,
            @RequestParam String anons,
            @RequestParam String full_text, Model model,
            @RequestParam("file") MultipartFile file) throws IOException {
        Post post = new Post(title, anons, full_text, user);

        if (file != null && !file.getOriginalFilename().isEmpty()) {   //проверка если вдруг файла нет
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {              //если uploadDir не существует
                uploadDir.mkdirs();                  //то мы ее создадим
            }
            String uuidFile = UUID.randomUUID().toString();                     // создание уникального имя файла
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFilename));
            post.setFilename(resultFilename);
        }
        postRepository.save(post);
        return "redirect:/blog";
    }

    @GetMapping("/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {  // !postRepository.existsById(id) если такого ID нет (False), Знак ! это делает//
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-details";
    }

    @GetMapping("/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        if (!postRepository.existsById(id)) {  // !postRepository.existsById(id) если такого ID нет (False), Знак ! это делает//
            return "redirect:/blog";
        }
        Optional<Post> post = postRepository.findById(id);
        ArrayList<Post> result = new ArrayList<>();
        post.ifPresent(result::add);
        model.addAttribute("post", result);
        return "blog-edit";
    }

    @PostMapping("/blog/{id}/edit")
    public String blogPostUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons, @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postRepository.save(post);
        return "redirect:/blog";
    }

    @PostMapping("/blog/{id}/remove")
    public String blogPostDelete(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        postRepository.delete(post);
        return "redirect:/blog";
    }


}
