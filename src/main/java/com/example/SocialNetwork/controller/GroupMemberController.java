package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.helpercalsses.MyRequest;
import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.MembershipRequest;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.MembershipRequestService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/groupmember")
public class GroupMemberController extends MyRequest {
    private final GroupMemberService memberService;
    private final MembershipRequestService requestService;
    private final UserService userService;
    private final SocialGroupService groupService;

    public GroupMemberController(GroupMemberService memberService,
                                 MembershipRequestService requestService,
                                 UserService userService,
                                 SocialGroupService groupService) {
        this.memberService =  memberService;
        this.requestService = requestService;
        this.userService = userService;
        this.groupService = groupService;
    }
    @GetMapping("/{id}")
    public List<String> showAllUsersForGroup(@PathVariable Long id){
        List<String> users = new ArrayList<>();
        List<Long> groupMember = memberService.getAllGroupMembers(id);

        for (Long aLong : groupMember) {
            users.add(userService.findByID(aLong).getUsername());
        }
        return users;
    }
    @PostMapping("/{id}")
    public String approveRequest(@PathVariable Long id) {
        MembershipRequest membershipRequest = requestService.getAllRequestsById(id);
        if (membershipRequest == null)
            return null;
        User currentUser = userService.findCurrentUser();
        SocialGroup socialGroup = membershipRequest.getSocialGroup();

        if(currentUser.getId() == socialGroup.getUser().getId()){

            Date ldt = new Date();
            GroupMember groupMember = new GroupMember();
            groupMember.setDateJoined(ldt);
            groupMember.setSocialGroup(socialGroup);
            User user = membershipRequest.getUser();
            groupMember.setUser(user);

            memberService.saveGroupMember(groupMember);
            requestService.deleteRequestById(id);

            return "Prihvatili ste zahtev za uclanjenje u grupu";
        }
        else
            return "Samo admin moze da prihvati zahtev, vi niste admin";
    }
    @DeleteMapping("/{id}")
    public String deleteMember(@PathVariable Long id){
        User user = userService.findCurrentUser();
        SocialGroup socialGroup = groupService.getSocialGroupById(id);
        if(user.getId() == socialGroup.getUser().getId()){
            return "Vi ste admin grupe, ne mozete je napustiti.";
        }
        else {
            memberService.deleteGroupMemberById(user.getId());
            return "Uspesno ste napustili grupu";
        }
    }
}

