package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apiSG")
public class SocialGroupController {
    private SocialGroupService groupService;
    private UserService userService;

    public SocialGroupController(SocialGroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9";
    }

    @PostMapping("/createGroup")
    public String createGroup(@RequestBody SocialGroup group) {
        User user = userService.findByID(1L);
        group.setUser(user);
        groupService.saveGroup(group);
        return "Usmesno sacuvana grupa ::))";
    }

    @GetMapping("/socialGroups")
    public List<SocialGroup> showAllSocialGroups(){
        return groupService.getAllSocialGroups();
    }

    @GetMapping("/socialGroups/{name}")
    public List<SocialGroup> getUserById(@PathVariable String name) {
        List<SocialGroup> socialGroup = groupService.getSocialGroupByName(name);
        return socialGroup;
    }
}
