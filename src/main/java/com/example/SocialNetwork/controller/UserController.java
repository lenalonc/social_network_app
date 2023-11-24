package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @PostMapping("/")
    public String saveUser(@RequestBody User user) {
        userService.saveUser(user);
        return "Bravo";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
        return "Bravo";
    }

    // NECE RADITI DOK SE NE SREDI SOCIALGROUP I NE POPUNI BAREM 1 RED U GROUPMEMBER TABELI
    @GetMapping("/")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    // NECE RADITI DOK SE NE SREDI SOCIALGROUP I NE POPUNI BAREM 1 RED U GROUPMEMBER TABELI
    @DeleteMapping("/{id}")
    public String deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);

        return "Bravo";
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        User user = userService.findByID(id);
        return user;
    }
}
