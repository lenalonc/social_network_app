package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.helpercalsses.MyRequest;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialgroup")
public class SocialGroupController extends MyRequest {
    private final SocialGroupService groupService;
    private final UserService userService;

    public SocialGroupController(SocialGroupService groupService,
                                 UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello Levi9";
    }

    @GetMapping("/")
    public List<SocialGroupDTO> showAllSocialGroups(){
        return groupService.getAllSocialGroups();
    }

    @PostMapping("/")
    public String createGroup(@RequestBody SocialGroup group) {
        User user = userService.findCurrentUser();
        group.setUser(user);
        groupService.saveGroup(group);
        return "Uspesno sacuvana grupa";
    }

    @DeleteMapping("/{id}")
    public String deleteSocialGroupById(@PathVariable Long id) {
        User currentU = userService.findCurrentUser();
        SocialGroup socialGroup = groupService.getSocialGroupById(id);

        if(currentU.getId() == socialGroup.getUser().getId())
        {
            groupService.deleteSocialGroupById(id);
            return "Uspesno ste obrisali grupu";
        }
        else {
            return "Niste ovlasceni da obrisete grupu";
        }
    }

    @GetMapping("/name/{name}")
    public List<SocialGroupDTO> getSocialGroupByName(@PathVariable String name) {
        return groupService.getSocialGroupByName(name);
    }
    @GetMapping("/id/{id}")
    public SocialGroupDTO getSocialGroupDTOById(@PathVariable Long id) {
        return groupService.getSocialGroupDTOById(id);
    }


}
