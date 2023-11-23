package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9 konferencijska sala uvek radi";
    }

    @PostMapping("/add")
    public String saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return "Bravo";
    }
}
