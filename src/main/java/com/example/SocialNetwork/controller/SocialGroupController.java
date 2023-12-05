package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.dtos.SocialGroupDTO;
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
@RequestMapping("/social_group")
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

    @GetMapping("/")
    public List<SocialGroupDTO> showAllSocialGroups(){
        return groupService.getAllSocialGroups();
    }

    @PostMapping("/")
    public ResponseEntity<?> createGroup(@RequestBody SocialGroup group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSocialGroupById(@PathVariable Long id, User user) {
        User currentUser = userService.findCurrentUser();
        groupService.deleteSocialGroupById(id, currentUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getSocialGroupByName(@PathVariable String name) {
        return ResponseEntity.ok(groupService.getSocialGroupByName(name));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getSocialGroupDTOById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getSocialGroupDTOById(id));
    }


    @PostMapping("/approve/{id}")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(groupMemberService.saveGroupMember(id));
    }

    @DeleteMapping("/leave_group/{id}")
    public ResponseEntity<?> leaveGroup(@PathVariable Long id){
        groupMemberService.removeCurrentUserFromGroupByGroupId(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all_users/{id}")
    public ResponseEntity<?> showAllUsersForGroup(@PathVariable Long id) {
        return ResponseEntity.ok(groupMemberService.getAllGroupMembers(id));
    }

    @PostMapping("/create_membership_request/{id}")
    public ResponseEntity<?> createMembershipRequest(@PathVariable Long id) {
        return ResponseEntity.ok(membershipRequestService.createMembershipRequest(id));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<?> joinGroup(@PathVariable Long id) {
        membershipRequestService.processJoinGroupRequest(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all_requests_for_group/{id}")
    public ResponseEntity<?> showAllRequests(@PathVariable Long id){
        return ResponseEntity.ok(membershipRequestService.getAllRequestsForSocialGroup(id));
    }
    @GetMapping("/membership_request/{id}")
    public ResponseEntity<?> getRequestsById(@PathVariable Long id) {
        return ResponseEntity.ok(membershipRequestService.getRequestsById(id));
    }

    @DeleteMapping("/delete_request/{id}")
    public ResponseEntity<?> deleteRequestById(@PathVariable Long id) {
        membershipRequestService.deleteRequestById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/change_name")
    public ResponseEntity<?> changeGroupName(@RequestParam Long id, @RequestParam String name){
        groupService.changeGroupName(id, name);
        return  ResponseEntity.ok().build();
    }

}
