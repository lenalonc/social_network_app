package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dto.SocialGroupDTO;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
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
    public ResponseEntity<?> createGroup(@RequestBody SocialGroup group) {
        return groupService.createGroup(group);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSocialGroupById(@PathVariable Long id, User user) {
        User currentUser = userService.findCurrentUser();
        return groupService.deleteSocialGroupById(id,currentUser);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<SocialGroupDTO>> getSocialGroupByName(@PathVariable String name) {
        return groupService.getSocialGroupByName(name);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<SocialGroupDTO> getSocialGroupDTOById(@PathVariable Long id) {
        return groupService.getSocialGroupDTOById(id);
    }


    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(groupMemberService.saveGroupMember(id));
    }

    @DeleteMapping("/deletemember/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id){
        groupMemberService.deleteGroupMemberByUserId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/allusers/{id}")
    public ResponseEntity<?> showAllUsersForGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupMemberService.getAllGroupMembers(id));
    }

    @PostMapping("/createmembershiprequest/{id}")
    public ResponseEntity<String> createMembershipRequest(@PathVariable Long id) {
        User currentUser = userService.findCurrentUser();
        return membershipRequestService.createMembershipRequest(id, currentUser);
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<String> joinGroup(@PathVariable Long id) {
        User currentUser = userService.findCurrentUser();
        return membershipRequestService.processJoinGroupRequest(id, currentUser);
    }

    @GetMapping("/allrequestsforgroup/{id}")
    public ResponseEntity<?> showAllRequests(@PathVariable Long id){
        return ResponseEntity.ok(membershipRequestService.getAllRequestsForSocialGroup(id));
    }
    @GetMapping("/membershiprequest/{id}")
    public ResponseEntity<?> getRequestsById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipRequestService.getRequestsById(id));
    }

    @DeleteMapping("/deleterequest/{id}")
    public ResponseEntity<?> deleteRequestById(@PathVariable Long id) {
        membershipRequestService.deleteRequestById(id);
        return ResponseEntity.ok().build();
    }

}
