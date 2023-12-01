package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.helper.MyRequest;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.MembershipRequestService;
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

    private final MembershipRequestService membershipRequestService;

    public SocialGroupController(SocialGroupService groupService,
                                 UserService userService,
                                 GroupMemberService groupMemberService,
                                 MembershipRequestService membershipRequestService) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupMemberService = groupMemberService;
        this.membershipRequestService = membershipRequestService;
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
    public ResponseEntity<String> createGroup(@RequestBody SocialGroup group) {
        return groupService.createGroup(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSocialGroupById(@PathVariable Long id) {
        return groupService.deleteSocialGroupById(id);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<SocialGroupDTO>> getSocialGroupByName(@PathVariable String name) {
        return groupService.getSocialGroupByName(name);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SocialGroupDTO> getSocialGroupDTOById(@PathVariable Long id) {
        return groupService.getSocialGroupDTOById(id);
    }


}
