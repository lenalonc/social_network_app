package com.example.SocialNetwork.controller;

import com.example.SocialNetwork.entities.GroupMember;
import com.example.SocialNetwork.entities.SocialGroup;
import com.example.SocialNetwork.entities.User;
import com.example.SocialNetwork.service.GroupMemberService;
import com.example.SocialNetwork.service.SocialGroupService;
import com.example.SocialNetwork.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RestController
@RequestMapping("/groupmember")
public class GroupMemberController {
    private final SocialGroupService groupService;
    private final UserService userService;
    private final GroupMemberService memberService;

    public GroupMemberController(SocialGroupService groupService,
                                 UserService userService,
                                 GroupMemberService memberService) {
        this.groupService = groupService;
        this.userService = userService;
        this.memberService =  memberService;
    }
    @PostMapping("/")
    public String approveRequest(GroupMember groupMember) {
        User user = userService.findByID(3L);
        groupMember.setUser(user);
        SocialGroup socialGroup = groupService.getSocialGroupById(2L);
        groupMember.setSocialGroup(socialGroup);
        Date ldt = new Date();
        groupMember.setDateJoined(ldt);
        memberService.saveGroupMember(groupMember);
        return "Vas zahtev za uclanjenje grupe je prihvacen!";
    }
}
