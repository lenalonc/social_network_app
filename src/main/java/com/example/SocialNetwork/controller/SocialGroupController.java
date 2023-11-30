package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.helpercalsses.MyRequest;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socialgroup")
public class SocialGroupController extends MyRequest {
    private final SocialGroupService groupService;
    private final UserService userService;
    private final GroupMemberService groupMemberService;

    public SocialGroupController(SocialGroupService groupService,
                                 UserService userService,
                                 GroupMemberService groupMemberService) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMemberService = groupMemberService;
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

    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(groupMemberService.saveGroupMember(id));
    }

    @DeleteMapping("/deletemember/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id){
        groupMemberService.deleteGroupMemberById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allusers/{id}")
    public ResponseEntity<?> showAllUsersForGroup(@PathVariable Long id){
        return ResponseEntity.ok(groupMemberService.getAllGroupMembers(id));
    }

}
