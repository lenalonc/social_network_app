package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialgroup")
public class SocialGroupController {
    private final SocialGroupService groupService;
    private final UserService userService;

    public SocialGroupController(SocialGroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9";
    }

    @PostMapping("/")
    public String createGroup(@RequestBody SocialGroup group) {
        User user = userService.findByID(1L);
        group.setUser(user);
        groupService.saveGroup(group);
        return "Usmesno sacuvana grupa";
    }

    @GetMapping("/all")
    public List<SocialGroup> showAllSocialGroups(){
        return groupService.getAllSocialGroups();
    }

    @GetMapping("all/{name}")
    public List<SocialGroup> getSocialGroupByName(@PathVariable String name) {
        return groupService.getSocialGroupByName(name);
    }
    @GetMapping("all/{id}")
    public SocialGroup getSocialGroupById(@PathVariable Long id) {
        return groupService.getSocialGroupById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteSocialGroupById(@PathVariable Long id) {
        groupService.deleteSocialGroupById(id);

        return "Usesno ste obrisali grupu";
    }

}
